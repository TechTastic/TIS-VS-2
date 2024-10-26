package io.github.techtastic.tisvs.module.altitude

import io.github.techtastic.tisvs.TISVS.MOD_ID
import io.github.techtastic.tisvs.util.HalfFloat
import li.cil.tis3d.api.API
import li.cil.tis3d.api.machine.Casing
import li.cil.tis3d.api.machine.Face
import li.cil.tis3d.api.machine.Port
import li.cil.tis3d.api.prefab.module.AbstractModuleWithRotation
import li.cil.tis3d.api.util.RenderContext
import li.cil.tis3d.util.Color
import net.minecraft.resources.ResourceLocation
import org.valkyrienskies.mod.common.getShipObjectManagingPos
import org.valkyrienskies.mod.common.util.toJOMLD

class AltitudeModule(casing: Casing, face: Face): AbstractModuleWithRotation(casing, face) {
    override fun step() {
        Port.VALUES.forEach { port ->
            val sendingPipe = casing.getSendingPipe(face, port)
            if (sendingPipe.isWriting) return@forEach
            sendingPipe.beginWrite(getAltitude())
        }
    }

    fun getAltitude(): Short {
        val level = casing.casingLevel
        val pos = casing.position
        val ship = level.getShipObjectManagingPos(pos) ?: return HalfFloat.toHalf(pos.y.toFloat())

        val worldPos = ship.transform.shipToWorld.transformPosition(pos.toJOMLD())
        return HalfFloat.toHalf(worldPos.y.toFloat())
    }

    override fun render(context: RenderContext) {
        if (!casing.isEnabled) return

        val poseStack = context.matrixStack
        poseStack.pushPose()
        this.rotateForRendering(poseStack)
        context.drawAtlasQuadUnlit(ResourceLocation(MOD_ID, "block/overlay/altitude_module"))

        if (context.closeEnoughForDetails(casing.position)) drawState(context)

        poseStack.popPose()
    }

    fun drawState(context: RenderContext) {
        val poseStack = context.matrixStack
        poseStack.pushPose()
        val font = API.normalFontRenderer
        poseStack.translate(3 / 16f, 5 / 16f, 0f)
        poseStack.scale(1 / 96f, 1 / 96f, 1f)

        val altitude = "${HalfFloat.toFloat(this.getAltitude())}"

        // Center Based on Text
        val width = font.width(altitude).toDouble()
        poseStack.translate(32 - width / 2, 10.0, 0.0)

        // Draw String
        context.drawString(font, altitude, Color.WHITE)

        poseStack.popPose()
    }
}