package io.github.techtastic.tisvs.forge

import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import io.github.techtastic.tisvs.TISVS.MOD_ID
import io.github.techtastic.tisvs.TISVS.init
import io.github.techtastic.tisvs.TISVS.initClient
import io.github.techtastic.tisvs.manual.TISVSManual
import io.github.techtastic.tisvs.module.TISVSModules
import io.github.techtastic.tisvs.serial.TISVSSerialInterfaces
import li.cil.tis3d.common.item.ModCreativeTabs
import net.minecraftforge.common.CreativeModeTabRegistry
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import thedarkcolour.kotlinforforge.KotlinModLoadingContext

@Mod(MOD_ID)
class TISVSForge {
    init {
        val bus = KotlinModLoadingContext.get().getKEventBus()
        EventBuses.registerModEventBus(MOD_ID, bus)

        bus.addListener(this::clientSetup)
        bus.addListener(this::onTabRegistry)

        TISVSModules.registerModules()
        TISVSSerialInterfaces.register()

        TISVSManual.registerContent()
        TISVSManual.registerPath()
        TISVSManual.registerTab()

        init()
    }

    private fun clientSetup(event: FMLClientSetupEvent) {
        initClient()
    }

    private fun onTabRegistry(event: BuildCreativeModeTabContentsEvent) {
        if (event.tab == ModCreativeTabs.COMMON.get()) {
            event.accept(TISVSModules.ALTITUDE_ITEM)
            event.accept(TISVSModules.DISTANCE_ITEM)
            event.accept(TISVSModules.GYROSCOPIC_ITEM)
            event.accept(TISVSModules.OMEGA_ITEM)
            event.accept(TISVSModules.VELOCITY_ITEM)
        }
    }
}
