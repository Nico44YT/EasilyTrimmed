package nico.easilytrimmed.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import nico.easilytrimmed.client.model.EasilyTrimmedModelLoadingPlugin;

public class EasilyTrimmedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModelLoadingPlugin.register(new EasilyTrimmedModelLoadingPlugin());
    }
}
