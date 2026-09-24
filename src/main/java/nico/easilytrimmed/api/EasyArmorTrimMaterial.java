package nico.easilytrimmed.api;

import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Map;

public class EasyArmorTrimMaterial {

    protected RegistryKey<ArmorTrimMaterial> materialRegistryKey;
    protected RegistryEntry<Item> itemRegistryEntry;
    protected Style style;
    protected float modelIndex;

    public EasyArmorTrimMaterial(Identifier id, Item item, int rgb, float modelIndex) {
        this(
                RegistryKey.of(RegistryKeys.TRIM_MATERIAL, id),
                item,
                rgb,
                modelIndex
        );
    }

    public EasyArmorTrimMaterial(RegistryKey<ArmorTrimMaterial> materialRegistryKey, Item item, int rgb, float modelIndex) {
        this(
                materialRegistryKey,
                Registries.ITEM.getEntry(item),
                Style.EMPTY.withColor(rgb & 0x00_FF_FF_FF),
                modelIndex
        );
    }

    public EasyArmorTrimMaterial(RegistryKey<ArmorTrimMaterial> materialRegistryKey, RegistryEntry<Item> itemRegistryEntry, Style style, float modelIndex) {
        this.materialRegistryKey = materialRegistryKey;
        this.itemRegistryEntry = itemRegistryEntry;
        this.style = style;
        this.modelIndex = modelIndex;
    }

    void bootstrap(Registerable<ArmorTrimMaterial> registerable) {
        ArmorTrimMaterial trimMaterial = new ArmorTrimMaterial(materialRegistryKey.getValue().getPath(), itemRegistryEntry, modelIndex, Map.of(),
                Text.translatable(Util.createTranslationKey("trim_material", materialRegistryKey.getValue())).fillStyle(style));

        registerable.register(materialRegistryKey, trimMaterial);
    }

    public RegistryKey<ArmorTrimMaterial> getMaterialRegistryKey() {
        return this.materialRegistryKey;
    }
}
