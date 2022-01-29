package me.chell.samsara.api.feature

import me.chell.samsara.api.value.Value

abstract class Feature(open val name: String) {
    abstract val values: List<Value<*>>
    abstract fun isEnabled(): Boolean
    abstract fun toggle()
    abstract fun getDisplayName(): String

    fun <T> getValue(name: String): Value<T>? = values.firstOrNull { it.name.equals(name, true) } as Value<T>?
}