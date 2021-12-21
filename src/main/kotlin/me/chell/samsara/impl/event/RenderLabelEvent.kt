package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event
import net.minecraft.entity.Entity

class RenderLabelEvent(val entity: Entity): Event.Cancelable()