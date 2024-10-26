package io.github.techtastic.tisvs.module.gyroscopic

import com.mojang.blaze3d.vertex.PoseStack
import io.github.techtastic.tisvs.TISVS.MOD_ID
import io.github.techtastic.tisvs.util.HalfFloat
import li.cil.manual.api.render.FontRenderer
import li.cil.tis3d.api.API
import li.cil.tis3d.api.machine.Casing
import li.cil.tis3d.api.machine.Face
import li.cil.tis3d.api.machine.Port
import li.cil.tis3d.api.prefab.module.AbstractModuleWithRotation
import li.cil.tis3d.api.util.RenderContext
import li.cil.tis3d.util.Color
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.joml.Vector3d
import org.joml.Vector3f
import org.valkyrienskies.mod.common.getShipObjectManagingPos

class GyroscopicModule(casing: Casing, face: Face): AbstractModuleWithRotation(casing, face) {
    val outputs = mutableMapOf(
        Pair(Port.UP, Output.ROLL),
        Pair(Port.RIGHT, Output.PITCH),
        Pair(Port.DOWN, Output.YAW),
        Pair(Port.LEFT, Output.QUAT_W)
    )

    enum class Output {
        QUAT_X,
        QUAT_Y,
        QUAT_Z,
        QUAT_W,
        ROLL,
        PITCH,
        YAW;

        fun get(level: Level, pos: BlockPos): Short {
            val ship = level.getShipObjectManagingPos(pos) ?: return HalfFloat.NaN

            val rot = ship.transform.shipToWorldRotation
            return HalfFloat.toHalf(when (this) {
                QUAT_X -> rot.x().toFloat()
                QUAT_Y -> rot.y().toFloat()
                QUAT_Z -> rot.z().toFloat()
                QUAT_W -> rot.w().toFloat()
                ROLL -> Math.toDegrees(rot.getEulerAnglesXYZ(Vector3d()).x).toFloat()
                PITCH -> Math.toDegrees(rot.getEulerAnglesXYZ(Vector3d()).z).toFloat()
                YAW -> Math.toDegrees(rot.getEulerAnglesXYZ(Vector3d()).y).toFloat()
            })
        }

        fun next() = when (this) {
            QUAT_X -> QUAT_Y
            QUAT_Y -> QUAT_Z
            QUAT_Z -> QUAT_W
            QUAT_W -> ROLL
            ROLL -> PITCH
            PITCH -> YAW
            YAW -> QUAT_X
        }

        fun render(poseStack: PoseStack, context: RenderContext, font: FontRenderer) {
            val (str, color) = when (this) {
                QUAT_X -> Pair("qX", Color.RED)
                QUAT_Y -> Pair("qY", Color.GREEN)
                QUAT_Z -> Pair("qZ", Color.BLUE)
                QUAT_W -> Pair("qW", Color.GRAY)
                ROLL -> Pair("R", Color.RED)
                PITCH -> Pair("P", Color.BLUE)
                YAW -> Pair("Y", Color.GREEN)
            }

            val width = font.width(str).toDouble()
            poseStack.translate(32 - width / 2, 10.0, 0.0)
            context.drawString(font, str, color)
            poseStack.translate(-32 + width / 2, -10.0, 0.0)
        }
    }

    override fun step() {
        Port.VALUES.forEach { port ->
            val sendingPipe = casing.getSendingPipe(face, port)
            if (sendingPipe.isWriting) return@forEach
            sendingPipe.beginWrite(outputs[port]?.get(casing.casingLevel, casing.position) ?: HalfFloat.NaN)
        }
    }

    override fun onInstalled(stack: ItemStack) {
        super.onInstalled(stack)

        if (!stack.hasTag()) return

        val tag = stack.orCreateTag
        outputs[Port.UP] = Output.valueOf(tag.getString("tisvs\$portUp"))
        outputs[Port.RIGHT] = Output.valueOf(tag.getString("tisvs\$portRight"))
        outputs[Port.DOWN] = Output.valueOf(tag.getString("tisvs\$portDown"))
        outputs[Port.LEFT] = Output.valueOf(tag.getString("tisvs\$portLeft"))
    }

    override fun onUninstalled(stack: ItemStack) {
        val tag = stack.orCreateTag
        tag.putString("tisvs\$portUp", outputs[Port.UP].toString())
        tag.putString("tisvs\$portRight", outputs[Port.RIGHT].toString())
        tag.putString("tisvs\$portDown", outputs[Port.DOWN].toString())
        tag.putString("tisvs\$portLeft", outputs[Port.LEFT].toString())

        super.onUninstalled(stack)
    }

    override fun save(tag: CompoundTag) {
        tag.putString("tisvs\$portUp", outputs[Port.UP].toString())
        tag.putString("tisvs\$portRight", outputs[Port.RIGHT].toString())
        tag.putString("tisvs\$portDown", outputs[Port.DOWN].toString())
        tag.putString("tisvs\$portLeft", outputs[Port.LEFT].toString())

        super.save(tag)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)

        outputs[Port.UP] = Output.valueOf(tag.getString("tisvs\$portUp"))
        outputs[Port.RIGHT] = Output.valueOf(tag.getString("tisvs\$portRight"))
        outputs[Port.DOWN] = Output.valueOf(tag.getString("tisvs\$portDown"))
        outputs[Port.LEFT] = Output.valueOf(tag.getString("tisvs\$portLeft"))
    }

    override fun use(player: Player, hand: InteractionHand, hit: Vec3): Boolean {
        if (hit.closerThan(Vec3(0.5, 0.0, 14.0 / 16.0), 0.25)) { // UP
            outputs[Port.UP] = outputs[Port.UP]?.next() ?: Output.QUAT_X
            return true
        }
        if (hit.closerThan(Vec3(14.0 / 16.0, 0.0, 0.5), 0.25)) { // RIGHT
            outputs[Port.RIGHT] = outputs[Port.RIGHT]?.next() ?: Output.QUAT_Y
            return true
        }
        if (hit.closerThan(Vec3(0.5, 0.0, 2.0 / 16.0), 0.25)) { // DOWN
            outputs[Port.DOWN] = outputs[Port.DOWN]?.next() ?: Output.QUAT_Z
            return true
        }
        if (hit.closerThan(Vec3(2.0 / 16.0, 0.0, 0.5), 0.25)) { // LEFT
            outputs[Port.LEFT] = outputs[Port.LEFT]?.next() ?: Output.QUAT_W
            return true
        }

        return super.use(player, hand, hit)
    }

    override fun render(context: RenderContext) {
        if (!casing.isEnabled) return

        val poseStack = context.matrixStack

        poseStack.pushPose()
        this.rotateForRendering(poseStack)
        context.drawAtlasQuadUnlit(ResourceLocation(MOD_ID, "block/overlay/gyroscopic_module"))

        val font = API.normalFontRenderer

        if (context.closeEnoughForDetails(casing.position))
            outputs.forEach { (port, output) ->
                val offset = when (port) {
                    Port.LEFT -> Vector3f(-1.5f / 16f, 5f / 16f, 0f)
                    Port.RIGHT -> Vector3f(7f / 16f, 5f / 16f, 0f)
                    Port.UP -> Vector3f(2.5f / 16f, 0.5f / 16f, 0f)
                    Port.DOWN -> Vector3f(2.5f / 16f, 9.5f / 16f, 0f)
                }

                poseStack.translate(offset.x, offset.y, offset.z)
                poseStack.scale(1 / 96f, 1 / 96f, 1f)

                output.render(poseStack, context, font)

                poseStack.scale(96f, 96f, 1f)
                poseStack.translate(-offset.x, -offset.y, -offset.z)
            }

        poseStack.popPose()
    }
}