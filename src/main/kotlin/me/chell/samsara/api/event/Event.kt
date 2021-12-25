package me.chell.samsara.api.event

open class Event {
    open class Cancelable(var canceled: Boolean = false): Event() {
        fun cancel() {
            canceled = true
        }
    }
}