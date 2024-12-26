package io.github.techtastic.tisvs.manual.custom

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Vector4f
import io.github.techtastic.tisvs.TISVS
import li.cil.manual.api.ManualModel
import li.cil.manual.api.prefab.tab.AbstractTab
import li.cil.manual.api.util.MatchResult
import li.cil.tis3d.client.manual.Manuals
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

class TISVSTab: AbstractTab("${ManualModel.LANGUAGE_KEY}/tisvs.md", Component.translatable("tisvs.manual.tab")) {
    override fun renderIcon(p0: PoseStack) {
        val position: Vector4f = Vector4f(0f, 0f, 0f, 1f)
        position.transform(p0.last().pose())

        val renderSystemPoseStack = RenderSystem.getModelViewStack()
        renderSystemPoseStack.pushPose()
        renderSystemPoseStack.translate(position.x().toDouble(), position.y().toDouble(), 0.0)

        Minecraft.getInstance().itemRenderer.renderGuiItem(TISVS.LOGO.get().defaultInstance, 0, 0)

        renderSystemPoseStack.popPose()
        RenderSystem.applyModelViewMatrix()


        // Unfuck GL state.
        RenderSystem.enableBlend()

    }

    override fun matches(manual: ManualModel): MatchResult {
        return if (manual == Manuals.MANUAL.get()) MatchResult.MATCH else MatchResult.MISMATCH
    }

    override fun sortOrder(): Int {
        return Int.MAX_VALUE
    }
}