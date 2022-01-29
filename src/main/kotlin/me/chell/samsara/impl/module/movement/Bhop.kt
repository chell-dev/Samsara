package me.chell.samsara.impl.module.movement

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.feature.Module
import me.chell.samsara.impl.event.PlayerTickEvent

class Bhop: Module("Bhop", Category.MOVEMENT, "So you don't have to hold space") {

    @EventHandler
    fun onTick(event: PlayerTickEvent) {
        if(Sprint.canSprint(player)) player.isSprinting = true
        if(canJump() && player.input.movementForward >= 0.8f) player.jump()
    }

    private fun canJump(): Boolean {
        if(player.input.jumping) return false
        return player.isOnGround && !player.isTouchingWater && !player.isInLava
    }
}