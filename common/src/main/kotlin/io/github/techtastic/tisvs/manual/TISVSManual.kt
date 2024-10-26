package io.github.techtastic.tisvs.manual

import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.tisvs.TISVS.MOD_ID
import io.github.techtastic.tisvs.manual.custom.TISVSTab
import io.github.techtastic.tisvs.manual.custom.TISVSDocumentProvider
import io.github.techtastic.tisvs.manual.custom.TISVSPathProvider
import li.cil.manual.api.util.Constants

object TISVSManual {
    private val TABS = DeferredRegister.create(MOD_ID, Constants.TAB_REGISTRY)
    private val PATH_PROVIDERS = DeferredRegister.create(MOD_ID, Constants.PATH_PROVIDER_REGISTRY)
    private val CONTENT_PROVIDERS = DeferredRegister.create(MOD_ID, Constants.DOCUMENT_PROVIDER_REGISTRY)

    fun registerContent() {
        CONTENT_PROVIDERS.register("content_provider", ::TISVSDocumentProvider)
        CONTENT_PROVIDERS.register()
    }

    fun registerPath() {
        PATH_PROVIDERS.register("path_provider", ::TISVSPathProvider)
        PATH_PROVIDERS.register()
    }

    fun registerTab() {
        TABS.register(MOD_ID, ::TISVSTab)
        TABS.register()
    }
}