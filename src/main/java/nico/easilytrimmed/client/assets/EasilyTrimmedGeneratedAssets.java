package nico.easilytrimmed.client.assets;

import com.google.common.base.Suppliers;
import com.google.gson.JsonObject;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.Identifier;
import nico.easilytrimmed.api.EasyArmorTrimMaterial;
import nico.easilytrimmed.api.EasyArmorTrimMaterialRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class EasilyTrimmedGeneratedAssets {
    public static List<EasyArmorTrimMaterialRegistry> REGISTRIES = new ArrayList<>();

    public static final Map<ModelIdentifier, BakedModel> MODEL_CACHE = new ConcurrentHashMap<>();
    public static final Supplier<Map<Identifier, String>> ARMOR_ITEMS = Suppliers.memoize(EasilyTrimmedGeneratedAssets::dumpTrimmableArmor);

    public static final Set<Identifier> ID_SET = new HashSet<>();

    public static Map<Identifier, Resource> create(Map<Identifier, Resource> map) {
        REGISTRIES.forEach(registry -> {
            registry.MATERIALS.forEach(material -> {
                ARMOR_ITEMS.get().forEach((pieceId, type) -> {
                    Identifier id = material.getMaterialRegistryKey().getValue().withPath(path -> pieceId.getPath() + "_" + path + "_trim");
                    Identifier fileId = id.withPath(path -> "models/item/" + path + ".json");

                    ID_SET.add(id);

                    map.put(fileId, createItemModel(pieceId, type, material).apply(null));
                });
            });
        });

        return map;
    }

    private static Map<Identifier, String> dumpTrimmableArmor() {
        Map<Identifier, String> map = new HashMap<>();

        Registries.ITEM.forEach(item -> {
            String result = isArmorItem(item);
            if (result != null) map.put(Registries.ITEM.getId(item), result);
        });

        return map;
    }

    private static String isArmorItem(Item item) {
        if (item instanceof ArmorItem armor) {

            EquipmentSlot slot = armor.getSlotType();

            return switch (slot) {
                case FEET -> "boots";
                case LEGS -> "leggings";
                case CHEST -> "chestplate";
                case HEAD -> "helmet";
                default -> null;
            };
        }

        return null;
    }

    private static Function<ResourcePack, Resource> createItemModel(Identifier pieceId, String type, EasyArmorTrimMaterial material) {
        JsonObject root = new JsonObject();

        root.addProperty("parent", pieceId.withPrefixedPath("item/").toString());

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", pieceId.withPrefixedPath("item/").toString());
        textures.addProperty("layer1", Identifier.of("minecraft", material.getMaterialRegistryKey().getValue().withPath(path -> type + "_trim_" + path).withPrefixedPath("trims/items/").getPath()).toString());

        root.add("textures", textures);

        return pack -> new Resource(
                pack,
                () -> new ByteArrayInputStream(root.toString().getBytes(StandardCharsets.UTF_8))
        );
    }

}
