package io.github.techtastic.tisvs.forge.module.gyroscopic

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
import org.valkyrienskies.mod.api.getShipManagingBlock

class GyroscopicModule(casing: Casing, face: Face): AbstractModuleWithRotation(casing, face) {
    val outputs = mutableMapOf(
        Pair(Port.UP, Output.QUAT_W),
        Pair(Port.RIGHT, Output.QUAT_X),
        Pair(Port.DOWN, Output.QUAT_Y),
        Pair(Port.LEFT, Output.QUAT_Z)
    )

    enum class Output {
        QUAT_X,
        QUAT_Y,
        QUAT_Z,
        QUAT_W;

        fun get(level: Level, pos: BlockPos): Short {
            val ship = level.getShipManagingBlock(pos) ?: return HalfFloat.NaN

            val rot = ship.transform.shipToWorldRotation
            return HalfFloat.toHalf(when (this) {
                QUAT_X -> rot.x().toFloat()
                QUAT_Y -> rot.y().toFloat()
                QUAT_Z -> rot.z().toFloat()
                QUAT_W -> rot.w().toFloat()
            })
        }

        fun next() = when (this) {
            QUAT_X -> QUAT_Y
            QUAT_Y -> QUAT_Z
            QUAT_Z -> QUAT_W
            QUAT_W -> QUAT_X
        }

        fun render(poseStack: PoseStack, context: RenderContext, font: FontRenderer) {
            val (str, color) = when (this) {
                QUAT_X -> Pair("qX", Color.RED)
                QUAT_Y -> Pair("qY", Color.GREEN)
                QUAT_Z -> Pair("qZ", Color.BLUE)
                QUAT_W -> Pair("qW", Color.GRAY)
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
        val uvHit = this.hitToUV(hit)

        if (uvHit.closerThan(Vec3(0.5, 0.25, 0.0), 0.15)) { // UP
            outputs[Port.UP] = outputs[Port.UP]?.next() ?: Output.QUAT_X
            return true
        } else if (uvHit.closerThan(Vec3(0.25, 0.5, 0.0), 0.15)) { // LEFT
            outputs[Port.LEFT] = outputs[Port.LEFT]?.next() ?: Output.QUAT_W
            return true
        } else if (uvHit.closerThan(Vec3(0.75, 0.5, 0.0), 0.15)) { // RIGHT
            outputs[Port.RIGHT] = outputs[Port.RIGHT]?.next() ?: Output.QUAT_Y
            return true
        } else if (uvHit.closerThan(Vec3(0.5, 0.75, 0.0), 0.15)) { // DOWN
            outputs[Port.DOWN] = outputs[Port.DOWN]?.next() ?: Output.QUAT_Z
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
                    Port.LEFT -> Vector3d(-1.5 / 16, 5 / 16.0, 0.0)
                    Port.RIGHT -> Vector3d(7 / 16.0, 5 / 16.0, 0.0)
                    Port.UP -> Vector3d(2.6 / 16, 0.5 / 16, 0.0)
                    Port.DOWN -> Vector3d(2.6 / 16, 9.5 / 16, 0.0)
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