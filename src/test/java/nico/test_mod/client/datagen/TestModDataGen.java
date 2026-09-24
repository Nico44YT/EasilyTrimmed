package nico.test_mod.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import nico.test_mod.client.datagen.provider.TestModArmorTrimAtlasProvider;
import nico.test_mod.client.datagen.provider.TestModItemTagProvider;
import nico.test_mod.client.datagen.provider.TestModRegistryDataGenerator;
import nico.test_mod.trims.ModTrimMaterials;

public class TestModDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(TestModItemTagProvider::new);
        pack.addProvider(TestModRegistryDataGenerator::new);
        pack.addProvider(TestModArmorTrimAtlasProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(
                RegistryKeys.TRIM_MATERIAL,
                ModTrimMaterials.REGISTRY::bootstrap
        );
    }
}
