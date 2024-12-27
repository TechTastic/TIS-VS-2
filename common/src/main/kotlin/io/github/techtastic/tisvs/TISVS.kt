package io.github.techtastic.tisvs

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.Registry
import net.minecraft.world.item.Item

object TISVS {
    const val MOD_ID: String = "tisvs"

    private val ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY)
    val LOGO = ITEMS.register("logo") { Item(Item.Properties()) }

    @JvmStatic
    fun init() {
        ITEMS.register()
        TISVSGameRules.register()
    }
}
