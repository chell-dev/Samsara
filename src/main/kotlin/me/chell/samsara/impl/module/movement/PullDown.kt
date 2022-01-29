package me.chell.samsara.impl.module.movement

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.feature.Module
import me.chell.samsara.impl.event.PlayerTickEvent
import net.minecraft.tag.FluidTags

class PullDown: Module("PullDown", Category.MOVEMENT, "Makes you fall faster.") {

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        if(player.isOnGround && player.getFluidHeight(FluidTags.WATER) == 0.0 && player.getFluidHeight(FluidTags.LAVA) == 0.0)
                player.addVelocity(0.0, -1.0, 0.0)
    }

}