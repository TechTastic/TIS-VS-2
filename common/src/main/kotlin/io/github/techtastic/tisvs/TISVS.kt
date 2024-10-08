package io.github.techtastic.tisvs

import io.github.techtastic.tisvs.module.TISVSModules

object TISVS {
    const val MOD_ID = "tisvs"

    @JvmStatic
    fun init() {
        TISVSModules.registerModuleItems()
    }

    @JvmStatic
    fun initClient() {
    }
}
