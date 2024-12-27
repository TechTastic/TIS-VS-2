package io.github.techtastic.tisvs.util

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import org.valkyrienskies.mod.common.getShipObjectManagingPos
import org.valkyrienskies.mod.common.util.toJOMLD

object VSUtils {
    fun Level.getShip(pos: BlockPos) =
        this.getShipObjectManagingPos(pos)

    fun BlockPos.toJOMLd() =
        this.toJOMLD()
}