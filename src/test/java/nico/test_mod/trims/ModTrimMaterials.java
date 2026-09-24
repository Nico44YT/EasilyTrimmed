package nico.test_mod.trims;

import net.minecraft.item.Items;
import nico.easilytrimmed.api.EasyArmorTrimMaterial;
import nico.easilytrimmed.api.EasyArmorTrimMaterialRegistry;
import nico.test_mod.TestMod;

public class ModTrimMaterials {
    public static final EasyArmorTrimMaterialRegistry REGISTRY = new EasyArmorTrimMaterialRegistry();

    public static final EasyArmorTrimMaterial ECHO_SHARD = REGISTRY.register(
            TestMod.id("echo_shard"),
            Items.ECHO_SHARD,
            0x0F_0F_FF,
            0.1F
    );

    public static void init() {
        System.out.println("Initialized Armor Trim Materials");
    }
}
