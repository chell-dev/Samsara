package me.chell.samsara.impl.event

import me.chell.samsara.Samsara
import me.chell.samsara.api.event.Event

class ChatEvent(var message: String): Event.Cancelable() {
    val isCommand
        get() = message.startsWith('/') || message.startsWith(Samsara.commandPrefix.value)
}