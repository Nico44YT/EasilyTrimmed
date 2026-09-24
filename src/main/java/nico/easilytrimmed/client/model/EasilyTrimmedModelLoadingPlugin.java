package nico.easilytrimmed.client.model;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import nico.easilytrimmed.client.assets.EasilyTrimmedGeneratedAssets;

import java.util.function.Consumer;

public class EasilyTrimmedModelLoadingPlugin implements ModelLoadingPlugin {
    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        Consumer<Identifier> modelAdder = id -> pluginContext.addModels(new ModelIdentifier(id, "inventory"));
        EasilyTrimmedGeneratedAssets.ID_SET.forEach(modelAdder);
    }
}
