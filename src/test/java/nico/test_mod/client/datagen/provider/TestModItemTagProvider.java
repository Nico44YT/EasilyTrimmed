package nico.test_mod.client.datagen.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class TestModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public TestModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        addTag(ItemTags.TRIM_MATERIALS, Items.ECHO_SHARD);
    }

    public void addTag(TagKey<Item> tagKey, ItemConvertible... itemConvertibles) {
        getOrCreateTagBuilder(tagKey).add(Arrays.stream(itemConvertibles).map(ItemConvertible::asItem).toArray(Item[]::new));
    }
}
