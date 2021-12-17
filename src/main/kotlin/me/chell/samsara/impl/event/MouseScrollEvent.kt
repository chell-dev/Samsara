package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event

class MouseScrollEvent(val amount: Double): Event.Cancelable()