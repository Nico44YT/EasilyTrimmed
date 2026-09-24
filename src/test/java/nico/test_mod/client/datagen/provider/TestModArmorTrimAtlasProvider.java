package nico.test_mod.client.datagen.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import nico.easilytrimmed.api.EasyArmorTrimAtlasProvider;
import nico.test_mod.trims.ModTrimMaterials;

import java.util.concurrent.CompletableFuture;

public class TestModArmorTrimAtlasProvider extends EasyArmorTrimAtlasProvider {
    public TestModArmorTrimAtlasProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void configure() {
        registerTrimMaterial(ModTrimMaterials.ECHO_SHARD);
    }

    @Override
    public String getName() {
        return "Trim Atlas Provider";
    }
}
