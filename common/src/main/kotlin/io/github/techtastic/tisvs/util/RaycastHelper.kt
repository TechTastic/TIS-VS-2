package io.github.techtastic.tisvs.util

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.ClipContext
import org.valkyrienskies.mod.common.util.toDoubles

object RaycastHelper {
    fun createContext(pos: BlockPos, direction: Direction, distance: Double): ClipContext {
        val dir = direction.normal.toDoubles().normalize()
        val origin = dir.add(pos.toDoubles())
        val end = origin.add(dir.x * distance, dir.y * distance, dir.z * distance)

        return ClipContext(
            origin,
            end,
            ClipContext.Block.OUTLINE,
            ClipContext.Fluid.NONE,
            null
        )
    }
}
