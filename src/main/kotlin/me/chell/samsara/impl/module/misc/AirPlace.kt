package me.chell.samsara.impl.module.misc

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.feature.Module
import me.chell.samsara.api.util.getBlockState
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.event.PlayerTickEvent
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

class AirPlace: Module("AirPlace", Category.MISC) {

    @Register val distance = Value("Distance", 4.5, min = 0.0, max = 7.0)

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        if(mc.options.keyUse.isPressed) {
            val raytrace = player.raycast(distance.value, 0f, false)
            if(raytrace != null && world.getBlockState(raytrace.pos).material.isReplaceable) {
                networkHandler.sendPacket(PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, BlockHitResult(raytrace.pos, Direction.UP, BlockPos(raytrace.pos), false)))
                player.swingHand(Hand.MAIN_HAND)
            }
        }
    }
}