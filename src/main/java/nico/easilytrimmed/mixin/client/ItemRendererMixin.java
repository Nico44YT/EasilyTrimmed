package nico.easilytrimmed.mixin.client;

import com.google.common.base.Suppliers;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import nico.easilytrimmed.client.assets.EasilyTrimmedGeneratedAssets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Unique
    private static final Supplier<Set<RegistryKey<ArmorTrimMaterial>>> materialsList = Suppliers.memoize(() -> {
        Set<RegistryKey<ArmorTrimMaterial>> materials = new HashSet<>();
        EasilyTrimmedGeneratedAssets.REGISTRIES.forEach(reg -> {
            reg.MATERIALS.forEach($ -> materials.add($.getMaterialRegistryKey()));
        });
        return materials;
    });

    @WrapOperation(method = "getModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/ModelOverrideList;apply(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;"))
    private BakedModel applyModelOverrides(
            ModelOverrideList overrideList,
            BakedModel model,
            ItemStack stack,
            ClientWorld world,
            LivingEntity entity,
            int seed,
            Operation<BakedModel> original
    ) {
        if (!stack.isIn(ItemTags.TRIMMABLE_ARMOR))
            return original.call(overrideList, model, stack, world, entity, seed);

        Optional<ArmorTrim> optional = ArmorTrim.getTrim(world.getRegistryManager(), stack);

        if (optional.isEmpty()) return original.call(overrideList, model, stack, world, entity, seed);
        ArmorTrim trim = optional.get();
        RegistryEntry<ArmorTrimMaterial> material = trim.getMaterial();
        var materialKey = material.getKey().orElse(null);

        if (materialKey == null || !materialsList.get().contains(materialKey))
            return original.call(overrideList, model, stack, world, entity, seed);

        Identifier modelId = materialKey.getValue()
                .withPath(path -> Registries.ITEM.getId(stack.getItem()).getPath() + "_" + path + "_trim");
        ModelIdentifier inventoryId = new ModelIdentifier(modelId, "inventory");

        return EasilyTrimmedGeneratedAssets.MODEL_CACHE.computeIfAbsent(inventoryId, id ->
                MinecraftClient.getInstance()
                        .getBakedModelManager()
                        .getModel(id)
        );
    }

    @Inject(method = "reload", at = @At("HEAD"))
    private void reload(CallbackInfo ci) {
        EasilyTrimmedGeneratedAssets.MODEL_CACHE.clear();
    }
}
