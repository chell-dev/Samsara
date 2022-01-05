package me.chell.samsara.impl.module.movement

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.module.Module
import me.chell.samsara.impl.event.PlayerTickEvent
import net.minecraft.tag.FluidTags

class Bhop: Module("Bhop", Category.MOVEMENT, "So you don't have to hold space") {

    @EventHandler
    fun onTick(event: PlayerTickEvent) {
        if(Sprint.canSprint(player)) player.isSprinting = true
        if(canJump() && player.input.movementForward >= 0.8f) player.jump()
    }

    private fun canJump(): Boolean {
        if(player.input.jumping) return false
        if(player.isOnGround) return true

        val a = if(player.isInLava) player.getFluidHeight(FluidTags.LAVA) else player.getFluidHeight(FluidTags.WATER)
        return (player.isTouchingWater && a > 0.0) && a > player.swimHeight
    }
}