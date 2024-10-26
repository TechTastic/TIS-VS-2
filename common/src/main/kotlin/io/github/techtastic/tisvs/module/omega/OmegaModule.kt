package io.github.techtastic.tisvs.module.omega

import io.github.techtastic.tisvs.TISVS.MOD_ID
import io.github.techtastic.tisvs.util.HalfFloat
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
import org.valkyrienskies.mod.common.getShipObjectManagingPos
import kotlin.math.floor

class OmegaModule(casing: Casing, face: Face): AbstractModuleWithRotation(casing, face) {
    var output = Output.X
    
    enum class Output {
        X,
        Y,
        Z;

        fun get(level: Level, pos: BlockPos): Short {
            val ship = level.getShipObjectManagingPos(pos) ?: return HalfFloat.NaN
            return HalfFloat.toHalf(when (this) {
                X -> ship.omega.x()
                Y -> ship.omega.y()
                Z -> ship.omega.z()
            }.toFloat())
        }

        fun next() = when (this) {
            X -> Y
            Y -> Z
            Z -> X
        }
    }

    override fun step() {
        if (!casing.isEnabled) return

        Port.VALUES.forEach { port ->
            val sendingPipe = casing.getSendingPipe(face, port)
            if (sendingPipe.isWriting) return@forEach
            sendingPipe.beginWrite(output.get(casing.casingLevel, casing.position))
        }
    }

    override fun onInstalled(stack: ItemStack) {
        super.onInstalled(stack)

        if (stack.hasTag())
            load(stack.orCreateTag)
    }

    override fun onUninstalled(stack: ItemStack) {
        save(stack.orCreateTag)

        super.onUninstalled(stack)
    }

    override fun save(tag: CompoundTag) {
        tag.putString("tisvs\$mode", output.toString())

        super.save(tag)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)

        output = Output.valueOf(tag.getString("tisvs\$mode"))
    }

    override fun use(player: Player, hand: InteractionHand, hit: Vec3): Boolean {
        if (player.isCrouching)
            return super.use(player, hand, hit)

        output = output.next()
        return true
    }

    override fun render(context: RenderContext) {
        if (!casing.isEnabled) return

        val poseStack = context.matrixStack

        poseStack.pushPose()
        this.rotateForRendering(poseStack)
        context.drawAtlasQuadUnlit(ResourceLocation(MOD_ID, "block/overlay/rotation_speed/${when (output) {
            Output.X -> "roll"
            Output.Y -> "yaw"
            Output.Z -> "pitch"
        }}"))

        val font = API.normalFontRenderer

        if (context.closeEnoughForDetails(casing.position)) {
            poseStack.translate(3 / 16f, 10 / 16f, 0f)
            poseStack.scale(1 / 96f, 1 / 96f, 1f)

            val omega = "${HalfFloat.toFloat(output.get(casing.casingLevel, casing.position))}"

            // Center Based on Text
            val width = font.width(omega).toDouble()
            poseStack.translate(32 - width / 2, 10.0, 0.0)

            // Draw String
            context.drawString(font, omega, Color.WHITE)
        }

        poseStack.popPose()
    }
}