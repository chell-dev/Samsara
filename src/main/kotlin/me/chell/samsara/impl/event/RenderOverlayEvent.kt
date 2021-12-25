package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event

class RenderOverlayEvent(val type: Type): Event.Cancelable() {
    enum class Type {
        WALL, WATER, FIRE
    }
}