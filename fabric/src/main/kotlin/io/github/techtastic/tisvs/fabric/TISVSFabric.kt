package io.github.techtastic.tisvs.fabric

import io.github.techtastic.tisvs.TISVS.init
import io.github.techtastic.tisvs.TISVS.initClient
import io.github.techtastic.tisvs.module.TISVSModules
import li.cil.tis3d.common.item.ModCreativeTabs
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import org.valkyrienskies.mod.fabric.common.ValkyrienSkiesModFabric

object TISVSFabric: ModInitializer {
    override fun onInitialize() {
        // force VS2 to load before TIS: VS
        ValkyrienSkiesModFabric().onInitialize()

        init()
        
        ItemGroupEvents.modifyEntriesEvent(ModCreativeTabs.COMMON.key).register { entries ->
            entries.accept(TISVSModules.ALTITUDE_ITEM.get())
            entries.accept(TISVSModules.DISTANCE_ITEM.get())
            entries.accept(TISVSModules.GYROSCOPIC_ITEM.get())
            entries.accept(TISVSModules.OMEGA_ITEM.get())
            entries.accept(TISVSModules.VELOCITY_ITEM.get())
        }
    }

    @Environment(EnvType.CLIENT)
    class Client : ClientModInitializer {
        override fun onInitializeClient() {
            initClient()
        }
    }
}
