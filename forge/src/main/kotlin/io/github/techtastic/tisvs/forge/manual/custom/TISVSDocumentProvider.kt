package io.github.techtastic.tisvs.forge.manual.custom

import io.github.techtastic.tisvs.TISVS.MOD_ID
import li.cil.manual.api.ManualModel
import li.cil.manual.api.prefab.provider.NamespaceDocumentProvider
import li.cil.tis3d.client.manual.Manuals
import net.minecraft.resources.ResourceLocation
import java.util.*

class TISVSDocumentProvider: NamespaceDocumentProvider(MOD_ID, "doc") {
    override fun matches(manual: ManualModel) =
        Objects.equals(manual, Manuals.MANUAL.get())

    override fun sortOrder() =
        Int.MAX_VALUE
}