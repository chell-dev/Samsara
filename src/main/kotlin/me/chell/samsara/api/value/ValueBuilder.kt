package me.chell.samsara.api.value

import java.util.function.Predicate

class ValueBuilder<T>(private val name: String, private val value: T) {
    private var description: String = "No description."
    private var displayName = name
    private var min: T? = null
    private var max: T? = null
    private var predicate = Predicate<Boolean> {true}

    fun description(description: String): ValueBuilder<T> {
        this.description = description
        return this
    }

    fun bounds(min: T, max: T): ValueBuilder<T> {
        this.min = min
        this.max = max
        return this
    }

    fun name(name: String): ValueBuilder<T> {
        displayName = name
        return this
    }

    fun visible(predicate: Predicate<Boolean>): ValueBuilder<T> {
        this.predicate = predicate
        return this
    }

    fun build(): Value<T> = Value(name, value, description, displayName, min, max, predicate)
}