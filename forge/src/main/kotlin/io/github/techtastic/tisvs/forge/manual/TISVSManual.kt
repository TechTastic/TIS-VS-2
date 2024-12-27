package io.github.techtastic.tisvs.forge.manual

import io.github.techtastic.tisvs.TISVS.MOD_ID
import io.github.techtastic.tisvs.forge.manual.custom.TISVSDocumentProvider
import io.github.techtastic.tisvs.forge.manual.custom.TISVSPathProvider
import io.github.techtastic.tisvs.forge.manual.custom.TISVSTab
import li.cil.manual.api.util.Constants
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister

object TISVSManual {
    private val TABS = DeferredRegister.create(Constants.TAB_REGISTRY, MOD_ID)
    private val PATH_PROVIDERS = DeferredRegister.create(Constants.PATH_PROVIDER_REGISTRY, MOD_ID)
    private val CONTENT_PROVIDERS = DeferredRegister.create(Constants.DOCUMENT_PROVIDER_REGISTRY, MOD_ID)

    val DOCUMENT_PROVIDER = CONTENT_PROVIDERS.register("content_provider", ::TISVSDocumentProvider)
    val PATH_PROVIDER = PATH_PROVIDERS.register("path_provider", ::TISVSPathProvider)
    val TAB = TABS.register(MOD_ID, ::TISVSTab)

    fun register(bus: IEventBus) {
        CONTENT_PROVIDERS.register(bus)
        PATH_PROVIDERS.register(bus)
        TABS.register(bus)
    }
}