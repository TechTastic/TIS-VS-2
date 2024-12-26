package io.github.techtastic.tisvs.serial.custom

import io.github.techtastic.tisvs.util.HalfFloat
import li.cil.tis3d.api.serial.SerialInterface
import li.cil.tis3d.api.serial.SerialInterfaceProvider
import li.cil.tis3d.api.serial.SerialProtocolDocumentationReference
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.decoration.ItemFrame
import net.minecraft.world.item.Items
import net.minecraft.world.item.MapItem
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import org.valkyrienskies.mod.common.getShipObjectManagingPos
import org.valkyrienskies.mod.common.util.toDoubles
import org.valkyrienskies.mod.common.util.toJOMLD
import org.valkyrienskies.mod.common.util.toMinecraft
import java.util.*

class FramedMapSerialInterfaceProvider: SerialInterfaceProvider {
    override fun matches(level: Level, pos: BlockPos, direction: Direction) = getFramedMap(level, pos, direction) != null

    override fun getInterface(level: Level, pos: BlockPos, direction: Direction): Optional<SerialInterface> {
        val frame = getFramedMap(level, pos, direction)
        return Optional.ofNullable(frame?.let { FramedMapSerialInterface(it) })
    }

    override fun getDocumentationReference(): Optional<SerialProtocolDocumentationReference> = Optional.empty()

    override fun stillValid(level: Level, pos: BlockPos, direction: Direction, serial: SerialInterface): Boolean {
        val frame = getFramedMap(level, pos, direction)
        return serial is FramedMapSerialInterface
                && frame != null && serial.frame.`is`(frame)
                && serial.frame.item.`is`(Items.FILLED_MAP)
    }

    fun getFramedMap(level: Level, pos: BlockPos, direction: Direction): ItemFrame? {
        val frames = level.getEntitiesOfClass(ItemFrame::class.java,
            AABB.ofSize(pos.toDoubles().add(0.5, 0.5, 0.5), 1.0, 1.0, 1.0)).filter{ frame ->
                frame.hasFramedMap() && frame.direction.opposite == direction }
        println(frames)
        return frames.firstOrNull()
    }

    private class FramedMapSerialInterface(val frame: ItemFrame): SerialInterface {
        var outputX = true

        override fun canWrite() = frame.hasFramedMap()

        override fun write(input: Short) {
            outputX = when (input) {
                0x0000.toShort() -> true
                0x0001.toShort() -> false
                else -> outputX.not()
            }
        }

        override fun canRead() = frame.hasFramedMap()

        override fun peek() = getOffset()

        override fun skip() {}

        override fun reset() {
            outputX = true
        }

        override fun save(tag: CompoundTag) {
            tag.putBoolean("tisvs\$outputX", outputX)
        }

        override fun load(tag: CompoundTag) {
            outputX = tag.getBoolean("tisvs\$outputX")
        }

        private fun getOffset(): Short {
            val level = frame.level as ServerLevel
            val pos = frame.onPos
            val ship = level.getShipObjectManagingPos(pos)

            val testPos = ship?.transform?.shipToWorld?.transformPosition(pos.toJOMLD())?.toMinecraft()
                ?: pos.toJOMLD().toMinecraft()
            val mapData = MapItem.getSavedData(frame.item, level) ?: return HalfFloat.NaN

            if (!isOnMap(testPos.x, testPos.z))
                return if (outputX)
                    if (testPos.x < mapData.x) HalfFloat.NEGATIVE_ZERO else HalfFloat.POSITIVE_INFINITY
                else
                    if (testPos.z < mapData.z) HalfFloat.NEGATIVE_ZERO else HalfFloat.POSITIVE_INFINITY

            return if (outputX)
                HalfFloat.toHalf(testPos.x.toFloat() - mapData.x)
            else
                HalfFloat.toHalf(testPos.z.toFloat() - mapData.z)
        }

        private fun isOnMap(x: Double, z: Double) =
            x >= -63.0f && z >= -63.0f && x <= 63.0f && z <= 63.0f
    }
}