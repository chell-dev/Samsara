package me.chell.samsara.api.value

import com.google.gson.annotations.Expose
import java.util.function.Predicate

data class Value<T>(
    @Expose val name: String,
    @Expose var value: T,
    val description: String = "No description.",
    @Expose var displayName: String = name,
    val sliderMin: T? = null,
    val sliderMax: T? = null,
    val visible: Predicate<Boolean> = Predicate {true}
    ) {
    constructor(): this("", Any() as T) // for json, do not use
}