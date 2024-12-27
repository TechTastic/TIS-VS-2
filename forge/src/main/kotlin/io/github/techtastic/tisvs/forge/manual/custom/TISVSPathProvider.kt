package io.github.techtastic.tisvs.forge.manual.custom

import io.github.techtastic.tisvs.TISVS.MOD_ID
import li.cil.manual.api.ManualModel
import li.cil.manual.api.prefab.provider.NamespacePathProvider
import li.cil.tis3d.client.manual.Manuals
import java.util.*

class TISVSPathProvider: NamespacePathProvider(MOD_ID, false) {
    override fun matches(manual: ManualModel) =
        Objects.equals(manual, Manuals.MANUAL.get())

    override fun sortOrder() =
        Int.MAX_VALUE
}