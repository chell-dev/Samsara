package me.chell.samsara.impl.module.movement

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.module.Module
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.event.PlayerTickEvent

class Sprint: Module("Sprint", Category.MOVEMENT, "Sprint automatically.") {

    @Register val legit = Value("Legit", true, "Use keybind")

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        if(legit.value && mc.currentScreen == null) {
            mc.options.keySprint.isPressed = true
        } else {
            if(player.forwardSpeed != 0F || player.sidewaysSpeed != 0F) {
                player.isSprinting = true
            }
        }
    }
}