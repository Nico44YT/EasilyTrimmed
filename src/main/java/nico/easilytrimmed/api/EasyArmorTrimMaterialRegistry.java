package nico.easilytrimmed.api;

import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import nico.easilytrimmed.client.assets.EasilyTrimmedGeneratedAssets;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EasyArmorTrimMaterialRegistry {
    public final Set<EasyArmorTrimMaterial> MATERIALS = new HashSet<>();

    public EasyArmorTrimMaterialRegistry() {
        EasilyTrimmedGeneratedAssets.REGISTRIES.add(this);
    }

    public EasyArmorTrimMaterial register(EasyArmorTrimMaterial material) {
        MATERIALS.add(material);
        return material;
    }

    public EasyArmorTrimMaterial register(Identifier id, Item item, int rgb, float modelIndex) {
        return register(new EasyArmorTrimMaterial(id, item, rgb, modelIndex));
    }

    public void bootstrap(Registerable<ArmorTrimMaterial> registerable) {
        MATERIALS.forEach(material -> material.bootstrap(registerable));
    }

    public void addRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.TRIM_MATERIAL, this::bootstrap);
    }
}
