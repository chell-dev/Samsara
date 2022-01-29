package me.chell.samsara.impl.module.combat

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.feature.Module
import me.chell.samsara.impl.event.EntityKnockbackEvent
import me.chell.samsara.impl.event.EntityPushedEvent

class Velocity: Module("Velocity", Category.COMBAT, "Anti Knockback/Push") {

    @EventHandler
    fun onKnockback(event: EntityKnockbackEvent) {
        if(event.entity == player) event.cancel()
    }

    @EventHandler
    fun onPush(event: EntityPushedEvent) {
        if(event.pushedEntity == player) event.cancel()
    }

}