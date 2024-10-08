package io.github.techtastic.tisvs.forge

import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import io.github.techtastic.tisvs.TISVS.MOD_ID
import io.github.techtastic.tisvs.TISVS.init
import io.github.techtastic.tisvs.TISVS.initClient
import io.github.techtastic.tisvs.module.TISVSModules
import io.github.techtastic.tisvs.serial.TISVSSerialInterfaces
import thedarkcolour.kotlinforforge.KotlinModLoadingContext

@Mod(MOD_ID)
class TISVSForge {
    init {
        val bus = KotlinModLoadingContext.get().getKEventBus()
        EventBuses.registerModEventBus(MOD_ID, bus)

        bus.addListener { event: FMLClientSetupEvent? ->
            clientSetup(
                event
            )
        }

        TISVSModules.registerModules()
        TISVSSerialInterfaces.register()
        init()
    }

    private fun clientSetup(event: FMLClientSetupEvent?) {
        initClient()
    }
}
