package io.github.techtastic.tisvs.fabric.mixin;

import io.github.techtastic.tisvs.module.TISVSModules;
import li.cil.tis3d.common.provider.ModuleProviders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModuleProviders.class)
public class MixinModuleProviders {
    @Inject(method = "initialize", at = @At("TAIL"), remap = false)
    private static void tisvs$registerModules(CallbackInfo ci) {
        TISVSModules.INSTANCE.registerModules();
    }
}
