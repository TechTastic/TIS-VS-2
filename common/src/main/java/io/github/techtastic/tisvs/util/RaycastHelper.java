package io.github.techtastic.tisvs.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

public class RaycastHelper {
    public static ClipContext createContext(BlockPos pos, Direction direction, double distance) {
        Vec3 dir = VectorConversionsMCKt.toDoubles(direction.getNormal()).normalize();
        Vec3 origin = dir.add(VectorConversionsMCKt.toDoubles(pos));
        Vec3 end = origin.add(dir.x * distance, dir.y * distance, dir.z * distance);

        return new ClipContext(
                origin,
                end,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                null
        );
    }
}
