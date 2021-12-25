package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event

open class MouseInputEvent: Event.Cancelable() {
    class Button(val button: Int, val action: Int, val mods: Int): MouseInputEvent()
    class Scroll(val scrollAmount: Double): MouseInputEvent()
}