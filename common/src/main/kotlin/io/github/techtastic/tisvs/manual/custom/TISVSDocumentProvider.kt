package io.github.techtastic.tisvs.manual.custom

import io.github.techtastic.tisvs.TISVS.MOD_ID
import li.cil.manual.api.ManualModel
import li.cil.manual.api.prefab.provider.NamespaceDocumentProvider
import li.cil.manual.api.util.MatchResult
import li.cil.tis3d.client.manual.Manuals

class TISVSDocumentProvider: NamespaceDocumentProvider(MOD_ID, "doc") {
    override fun matches(manual: ManualModel): MatchResult {
        return if (manual == Manuals.MANUAL.get()) MatchResult.MATCH else MatchResult.MISMATCH
    }

    override fun sortOrder(): Int {
        return Int.MAX_VALUE
    }
}