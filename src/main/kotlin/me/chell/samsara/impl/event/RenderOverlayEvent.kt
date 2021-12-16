package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event

class RenderOverlayEvent(val type: Type, var canceled: Boolean = false): Event() {
    fun cancel() {
        canceled = true
    }

    enum class Type {
        WALL, WATER, FIRE
    }
}