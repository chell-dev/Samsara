package me.chell.samsara.impl.module.misc

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.module.Module
import me.chell.samsara.impl.event.PlayerTickEvent
import net.minecraft.item.EnderPearlItem
import net.minecraft.util.Hand

class PearlBind: Module("PearlBind", Category.MISC, "Throw pearl when enabled") {

    private var phase = Phase.SWITCH
    private var oldSlot = -1

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        when(phase) {
            Phase.SWITCH -> {
                for(i in 0..8) {
                    if(player.inventory.main[i].item is EnderPearlItem) {
                        oldSlot = player.inventory.selectedSlot
                        player.inventory.selectedSlot = i
                        phase = Phase.THROW
                        return
                    }
                }
                toggle()
            }
            Phase.THROW -> {
                interactionManager.interactItem(player, world, Hand.MAIN_HAND)
                player.swingHand(Hand.MAIN_HAND)
                phase = Phase.RETURN
            }
            Phase.RETURN -> {
                if(oldSlot != -1)
                    player.inventory.selectedSlot = oldSlot
                phase = Phase.SWITCH
                oldSlot = -1
                toggle()
            }
        }
    }

    enum class Phase {
        SWITCH, THROW, RETURN
    }
}