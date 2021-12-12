package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event

class KeyInputEvent(val key: Int, val action: Int, val modifiers: Int): Event()