package io.github.techtastic.tisvs.fabric.mixin;

import io.github.techtastic.tisvs.serial.TISVSSerialInterfaces;
import li.cil.tis3d.common.provider.SerialInterfaceProviders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SerialInterfaceProviders.class)
public class MixinSerialInterfaceProviders {
    @Inject(method = "initialize", at = @At("TAIL"), remap = false)
    private static void tisvs$registerInterfaces(CallbackInfo ci) {
        TISVSSerialInterfaces.INSTANCE.register();
    }
}
