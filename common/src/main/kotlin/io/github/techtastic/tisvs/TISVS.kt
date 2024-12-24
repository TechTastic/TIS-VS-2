package io.github.techtastic.tisvs

import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.tisvs.module.TISVSModules
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item

object TISVS {
    const val MOD_ID = "tisvs"

    private val ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM)
    val LOGO = ITEMS.register("logo") { Item(Item.Properties()) }

    @JvmStatic
    fun init() {
        TISVSModules.registerModuleItems()

        ITEMS.register()
        TISVSGameRules.register()
    }

    @JvmStatic
    fun initClient() {
    }
}
