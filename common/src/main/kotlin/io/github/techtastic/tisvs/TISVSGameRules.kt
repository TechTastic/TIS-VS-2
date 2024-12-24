package io.github.techtastic.tisvs

import net.minecraft.world.level.GameRules

object TISVSGameRules {
    val MAX_DISTANCE = GameRules.register("maxDistance", GameRules.Category.MISC, GameRules.IntegerValue.create(10))

    fun register() {}
}