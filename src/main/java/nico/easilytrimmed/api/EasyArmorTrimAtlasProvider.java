package nico.easilytrimmed.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class EasyArmorTrimAtlasProvider implements DataProvider {
    protected final DataOutput.PathResolver atlasFolderPathResolver;
    protected final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;

    private final Set<RegistryKey<ArmorTrimMaterial>> materials;

    public EasyArmorTrimAtlasProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        this.atlasFolderPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "atlases");
        this.registriesFuture = registriesFuture;

        this.materials = new HashSet<>();

        configure();
    }

    abstract public void configure();

    @SafeVarargs
    public final void registerTrimMaterial(RegistryKey<ArmorTrimMaterial>... trimMaterials) {
        Collections.addAll(this.materials, trimMaterials);
    }

    public final void registerTrimMaterial(EasyArmorTrimMaterial... materials) {
        for (EasyArmorTrimMaterial material : materials) {
            registerTrimMaterial(material.getMaterialRegistryKey());
        }
    }

    protected CompletableFuture<?> createArmorTrimsAtlas(RegistryWrapper.WrapperLookup registries, DataWriter writer) {
        JsonObject rootObject = new JsonObject();
        JsonArray sourcesArray = new JsonArray();

        JsonObject object1 = new JsonObject();
        object1.addProperty("type", "paletted_permutations");

        //region//Textures
        JsonArray textureArray = new JsonArray();

        var lookup = registries.createRegistryLookup().getOrThrow(RegistryKeys.TRIM_PATTERN);
        RegistryWrapper.Impl<ArmorTrimPattern> trimPatterns = (RegistryWrapper.Impl<ArmorTrimPattern>) lookup;
        trimPatterns.streamKeys().forEach(armorTrimPatternReference -> {
            var pattern = lookup.getOrThrow(armorTrimPatternReference).value();
            var assetId = pattern.assetId();
            textureArray.add(assetId.withPath(path -> "trims/models/armor/" + path).toString().replace("minecraft:", ""));
            textureArray.add(assetId.withPath(path -> "trims/models/armor/" + path + "_leggings").toString().replace("minecraft:", ""));
        });

        object1.add("textures", textureArray);
        //endregion
        object1.addProperty("palette_key", "trims/color_palettes/trim_palette");
        //region//Permutations
        JsonObject permutationsObject = new JsonObject();

        for (RegistryKey<ArmorTrimMaterial> registryKey : materials) {
            ArmorTrimMaterial material = registries.createRegistryLookup().getOrThrow(RegistryKeys.TRIM_MATERIAL).getOrThrow(registryKey).value();
            Identifier value = registryKey.getValue();
            if (!registryKey.getValue().getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
                permutationsObject.addProperty(value.getPath(), value.withPath(path -> "trims/color_palettes/" + path).toString());
            } else {
                permutationsObject.addProperty(material.assetName(), "trims/color_palettes/" + material.assetName());
            }
        }

        object1.add("permutations", permutationsObject);
        //endregion
        sourcesArray.add(object1);
        rootObject.add("sources", sourcesArray);

        return DataProvider.writeToPath(writer, rootObject, atlasFolderPathResolver.resolve(Identifier.of("minecraft", "armor_trims"), "json"));
    }

    protected CompletableFuture<?> createBlocksAtlas(RegistryWrapper.WrapperLookup registries, DataWriter writer) {
        final String[] TEXTURES = {
                "trims/items/leggings_trim",
                "trims/items/chestplate_trim",
                "trims/items/helmet_trim",
                "trims/items/boots_trim"
        };

        JsonObject rootObject = new JsonObject();
        JsonArray sourcesArray = new JsonArray();

        JsonObject object1 = new JsonObject();
        object1.addProperty("type", "paletted_permutations");

        JsonArray textureArray = new JsonArray();
        for (String texture : TEXTURES) textureArray.add(texture);

        object1.add("textures", textureArray);
        object1.addProperty("palette_key", "trims/color_palettes/trim_palette");
        //region//Permutations
        JsonObject permutationsObject = new JsonObject();

        for (RegistryKey<ArmorTrimMaterial> registryKey : materials) {
            ArmorTrimMaterial material = registries.createRegistryLookup().getOrThrow(RegistryKeys.TRIM_MATERIAL).getOrThrow(registryKey).value();
            Identifier value = registryKey.getValue();
            if (!registryKey.getValue().getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
                permutationsObject.addProperty(value.getPath(), value.withPath(path -> "trims/color_palettes/" + path).toString());
            } else {
                permutationsObject.addProperty(material.assetName(), "trims/color_palettes/" + material.assetName());
            }
        }

        object1.add("permutations", permutationsObject);
        //endregion

        sourcesArray.add(object1);
        rootObject.add("sources", sourcesArray);

        return DataProvider.writeToPath(writer, rootObject, atlasFolderPathResolver.resolve(Identifier.of("minecraft", "blocks"), "json"));
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return registriesFuture.thenCompose(registries ->
                CompletableFuture.allOf(
                        createBlocksAtlas(registries, writer),
                        createArmorTrimsAtlas(registries, writer)
                )
        );
    }
}
