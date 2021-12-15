package me.chell.samsara.impl.module.movement

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.module.Module
import me.chell.samsara.impl.event.PlayerTickEvent

class PullDown: Module("PullDown", Category.MOVEMENT) {

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        if(player.isOnGround) player.addVelocity(0.0, -1.0, 0.0)
    }

}