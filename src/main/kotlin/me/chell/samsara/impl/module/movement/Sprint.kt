package me.chell.samsara.impl.module.movement

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.feature.Module
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.event.PlayerTickEvent
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.effect.StatusEffects

class Sprint: Module("Sprint", Category.MOVEMENT, "Sprint automatically.") {

    @Register val legit = Value("Legit", true)

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        if(legit.value) {
            if(canSprint(player)) player.isSprinting = true
        } else {
            if(player.forwardSpeed != 0F || player.sidewaysSpeed != 0F) {
                player.isSprinting = true
            }
        }
    }

    companion object {
        fun canSprint(player: ClientPlayerEntity): Boolean {
            return !player.isSprinting
                    && (!player.isTouchingWater || player.isSubmergedInWater)
                    && player.input.movementForward.toDouble() >= 0.8
                    && (player.hungerManager.foodLevel.toFloat() > 6.0f || player.abilities.allowFlying)
                    && !player.isUsingItem
                    && !player.hasStatusEffect(StatusEffects.BLINDNESS)
        }
    }
}