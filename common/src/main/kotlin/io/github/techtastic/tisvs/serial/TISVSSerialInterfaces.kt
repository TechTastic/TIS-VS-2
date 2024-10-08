package io.github.techtastic.tisvs.serial

import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.tisvs.TISVS.MOD_ID
import li.cil.tis3d.api.serial.SerialInterfaceProvider
import io.github.techtastic.tisvs.serial.custom.FramedMapSerialInterfaceProvider

object TISVSSerialInterfaces {
    private val INTERFACES = DeferredRegister.create(MOD_ID, SerialInterfaceProvider.REGISTRY)

    val FRAMED_MAP_INTERFACE = INTERFACES.register("framed_map", ::FramedMapSerialInterfaceProvider)

    fun register() {
        INTERFACES.register()
    }
}