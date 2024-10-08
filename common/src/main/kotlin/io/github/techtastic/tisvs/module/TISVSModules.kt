package io.github.techtastic.tisvs.module

import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.tisvs.TISVS.MOD_ID
import io.github.techtastic.tisvs.module.altitude.AltitudeModule
import li.cil.tis3d.api.module.ModuleProvider
import li.cil.tis3d.common.item.ModuleItem
import li.cil.tis3d.common.provider.module.SimpleModuleProvider
import net.minecraft.core.registries.Registries

object TISVSModules {
    private val MODULES = DeferredRegister.create(MOD_ID, ModuleProvider.REGISTRY)
    private val ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM)

    val ALTITUDE_ITEM = ITEMS.register("altitude_module", ::ModuleItem)
    val ALTITUDE_MODULE = MODULES.register("altitude_module") { SimpleModuleProvider(ALTITUDE_ITEM, ::AltitudeModule) }

    fun registerModules() {
        MODULES.register()
    }

    fun registerModuleItems() {
        ITEMS.register()
    }
}