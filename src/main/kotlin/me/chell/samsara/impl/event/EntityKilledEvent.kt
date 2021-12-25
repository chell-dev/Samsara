package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event
import net.minecraft.entity.LivingEntity

class EntityKilledEvent(val target: LivingEntity): Event()