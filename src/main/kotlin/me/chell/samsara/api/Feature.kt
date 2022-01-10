package me.chell.samsara.api

import me.chell.samsara.api.value.Value

abstract class Feature(open val name: String) {
    abstract val values: List<Value<*>>
    abstract fun isEnabled(): Boolean
    abstract fun toggle()
    abstract fun getDisplayName(): String
}