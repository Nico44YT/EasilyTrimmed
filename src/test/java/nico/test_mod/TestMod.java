package nico.test_mod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import nico.test_mod.trims.ModTrimMaterials;

public class TestMod implements ModInitializer {
    public static final String MOD_ID = "test_mod";

    @Override
    public void onInitialize() {
        ModTrimMaterials.init();
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
