package io.github.techtastic.tisvs.manual.custom

import io.github.techtastic.tisvs.TISVS
import li.cil.manual.api.ManualModel
import li.cil.manual.api.prefab.tab.AbstractTab
import li.cil.manual.api.util.MatchResult
import li.cil.tis3d.client.manual.Manuals
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

class TISVSTab: AbstractTab("${ManualModel.LANGUAGE_KEY}/tisvs.md", Component.translatable("tisvs.manual.tab")) {
    override fun renderIcon(p0: GuiGraphics) {
        p0.renderItem(TISVS.LOGO.get().defaultInstance, 0, 0)
    }

    override fun matches(manual: ManualModel): MatchResult {
        return if (manual == Manuals.MANUAL.get()) MatchResult.MATCH else MatchResult.MISMATCH
    }

    override fun sortOrder(): Int {
        return Int.MAX_VALUE
    }
}