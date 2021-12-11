package me.chell.samsara.api.value

import java.util.function.Predicate

data class Value<T>(
    val name: String,
    var value: T,
    val displayName: String = name,
    val sliderMin: T? = null,
    val sliderMax: T? = null,
    val visible: Predicate<Boolean> = Predicate {true}
    )