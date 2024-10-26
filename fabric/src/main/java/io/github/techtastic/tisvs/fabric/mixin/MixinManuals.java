package io.github.techtastic.tisvs.fabric.mixin;

import io.github.techtastic.tisvs.manual.TISVSManual;
import li.cil.tis3d.client.manual.Manuals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Manuals.class)
public class MixinManuals {
    @Inject(method = "initialize", at = @At("TAIL"), remap = false)
    private static void tisvs$registerContent(CallbackInfo ci) {
        TISVSManual.INSTANCE.registerContent();
        TISVSManual.INSTANCE.registerPath();
        TISVSManual.INSTANCE.registerTab();
    }
}
