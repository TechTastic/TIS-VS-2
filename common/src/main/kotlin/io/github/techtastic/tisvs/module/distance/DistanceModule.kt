package io.github.techtastic.tisvs.module.distance

import io.github.techtastic.tisvs.TISVS.MOD_ID
import io.github.techtastic.tisvs.util.HalfFloat
import io.github.techtastic.tisvs.util.RaycastHelper
import li.cil.tis3d.api.machine.Casing
import li.cil.tis3d.api.machine.Face
import li.cil.tis3d.api.machine.Port
import li.cil.tis3d.api.prefab.module.AbstractModuleWithRotation
import li.cil.tis3d.api.util.RenderContext
import li.cil.tis3d.util.Color
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.HitResult

class DistanceModule(casing: Casing, face: Face): AbstractModuleWithRotation(casing, face) {
    var distance = 10.0

    override fun step() {
        this.stepInput()
        this.stepOutput()
    }

    private fun stepInput() {
        Port.VALUES.forEach { port ->
            val receiving = casing.getReceivingPipe(face, port)
            if (!receiving.isReading) receiving.beginRead()
            if (receiving.canTransfer())
                distance = HalfFloat.toFloat(receiving.read()).toDouble()
        }
    }

    private fun stepOutput() {
        Port.VALUES.forEach { port ->
            val sendingPipe = casing.getSendingPipe(face, port)
            if (sendingPipe.isWriting) return@forEach
            sendingPipe.beginWrite(if (hitSomething()) 1.toShort() else 0.toShort())
        }
    }

    private fun hitSomething(): Boolean {
        val level = casing.casingLevel
        val result = level.clip(RaycastHelper.createContext(casing.position, Face.toDirection(face), distance))

        return result.type != HitResult.Type.MISS
                && !level.getBlockState(result.blockPos).isAir
    }

    override fun onInstalled(stack: ItemStack) {
        super.onInstalled(stack)

        if (!stack.hasTag()) {
            distance = 10.0
            return
        }

        val tag = stack.orCreateTag
        distance = if (tag.contains("tisvs\$distance"))
            tag.getDouble("tisvs\$distance")
        else
            10.0
    }

    override fun onUninstalled(stack: ItemStack) {
        stack.orCreateTag.putDouble("tisvs\$distance", distance)

        super.onUninstalled(stack)
    }

    override fun save(tag: CompoundTag) {
        super.save(tag)

        tag.putDouble("tisvs\$distance", distance)
    }

    override fun load(tag: CompoundTag) {
        distance = tag.getDouble("tisvs\$distance")

        super.load(tag)
    }

    override fun render(context: RenderContext) {
        if (!casing.isEnabled) return

        val poseStack = context.matrixStack

        poseStack.pushPose()

        this.rotateForRendering(poseStack)

        if (!context.closeEnoughForDetails(casing.position))
            context.drawAtlasQuadUnlit(ResourceLocation(MOD_ID, "block/overlay/distance_module"))
        else
            context.drawAtlasQuadUnlit(ResourceLocation(MOD_ID, "block/overlay/distance_module"), if (hitSomething()) Color.GREEN else Color.RED)

        poseStack.popPose()
    }
}