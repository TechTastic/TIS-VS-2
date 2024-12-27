package io.github.techtastic.tisvs.forge.serial

import io.github.techtastic.tisvs.TISVS.MOD_ID
import io.github.techtastic.tisvs.forge.serial.custom.FramedMapSerialInterfaceProvider
import li.cil.tis3d.api.serial.SerialInterfaceProvider
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister

object TISVSSerialInterfaces {
    private val INTERFACES = DeferredRegister.create(SerialInterfaceProvider.REGISTRY, MOD_ID)

    val FRAMED_MAP_INTERFACE = INTERFACES.register("framed_map", ::FramedMapSerialInterfaceProvider)

    fun register(bus: IEventBus) {
        INTERFACES.register(bus)
    }
}