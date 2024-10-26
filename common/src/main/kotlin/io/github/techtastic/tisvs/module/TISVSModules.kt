package io.github.techtastic.tisvs.module

import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.tisvs.TISVS.MOD_ID
import io.github.techtastic.tisvs.module.altitude.AltitudeModule
import io.github.techtastic.tisvs.module.distance.DistanceModule
import io.github.techtastic.tisvs.module.gyroscopic.GyroscopicModule
import io.github.techtastic.tisvs.module.omega.OmegaModule
import io.github.techtastic.tisvs.module.velocity.VelocityModule
import li.cil.tis3d.api.module.ModuleProvider
import li.cil.tis3d.common.item.ModuleItem
import li.cil.tis3d.common.provider.module.SimpleModuleProvider
import net.minecraft.core.registries.Registries

object TISVSModules {
    private val MODULES = DeferredRegister.create(MOD_ID, ModuleProvider.REGISTRY)
    private val ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM)

    val ALTITUDE_ITEM = ITEMS.register("altitude_module", ::ModuleItem)
    val ALTITUDE_MODULE = MODULES.register("altitude_module") { SimpleModuleProvider(ALTITUDE_ITEM, ::AltitudeModule) }

    val DISTANCE_ITEM = ITEMS.register("distance_module", ::ModuleItem)
    val DISTANCE_MODULE = MODULES.register("distance_module") { SimpleModuleProvider(DISTANCE_ITEM, ::DistanceModule) }

    val GYROSCOPIC_ITEM = ITEMS.register("gyroscopic_module", ::ModuleItem)
    val GYROSCOPIC_MODULE = MODULES.register("gyroscopic_module") { SimpleModuleProvider(GYROSCOPIC_ITEM, ::GyroscopicModule) }

    val OMEGA_ITEM = ITEMS.register("omega_module", ::ModuleItem)
    val OMEGA_MODULE = MODULES.register("omega_module") { SimpleModuleProvider(OMEGA_ITEM, ::OmegaModule) }

    val VELOCITY_ITEM = ITEMS.register("velocity_module", ::ModuleItem)
    val VELOCITY_MODULE = MODULES.register("velocity_module") { SimpleModuleProvider(VELOCITY_ITEM, ::VelocityModule) }

    fun registerModules() {
        MODULES.register()
    }

    fun registerModuleItems() {
        ITEMS.register()
    }
}