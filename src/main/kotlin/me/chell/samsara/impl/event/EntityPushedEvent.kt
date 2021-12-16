package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event
import net.minecraft.entity.Entity

class EntityPushedEvent(val pushedEntity: Entity, val pushingEntity: Entity): Event.Cancelable()