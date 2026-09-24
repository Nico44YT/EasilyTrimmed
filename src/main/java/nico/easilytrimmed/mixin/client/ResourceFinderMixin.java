package nico.easilytrimmed.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import nico.easilytrimmed.client.assets.EasilyTrimmedGeneratedAssets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.function.Predicate;

@Mixin(ResourceFinder.class)
public abstract class ResourceFinderMixin {
    @WrapOperation(method = "findResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;findResources(Ljava/lang/String;Ljava/util/function/Predicate;)Ljava/util/Map;"))
    private Map<Identifier, Resource> f(ResourceManager instance, String s, Predicate<Identifier> identifierPredicate, Operation<Map<Identifier, Resource>> original) {
        if (!s.equals("models")) return original.call(instance, s, identifierPredicate);

        return EasilyTrimmedGeneratedAssets.create(original.call(instance, s, identifierPredicate));
    }
}
