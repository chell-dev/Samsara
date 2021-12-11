package me.chell.samsara.api.module

import me.chell.samsara.Samsara
import me.chell.samsara.api.Loadable
import me.chell.samsara.api.value.Bind
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.api.value.ValueBuilder
import kotlin.reflect.full.memberProperties

abstract class Module(
    val name: String,
    val category: Category
    ): Loadable {

    val values = mutableListOf<Value<*>>()

    @Register val displayName: Value<String> = ValueBuilder("Display Name", name).visible{false}.build()
    @Register val enabled: Value<Boolean> = ValueBuilder("Enabled", false).visible{false}.build()
    @Register val bind: Value<Bind> = Value("Bind", Bind(0))

    init {
        for(p in this::class.memberProperties) {
            if(p.annotations.isNotEmpty()) {
                if(p.annotations[0].annotationClass == Register::class)
                    values.add(p.getter.call(this) as Value<*>)
            }
        }
    }

    fun toggle() {
        if(enabled.value) {
            onDisable()
            enabled.value = false
        } else {
            onEnable()
            enabled.value = true
        }
    }

    open fun onEnable() {
        Samsara.instance.eventManager.register(this)
    }
    open fun onDisable() {
        Samsara.instance.eventManager.unregister(this)
    }

    fun <T> getValue(name: String): Value<T> = values.firstOrNull { it.name.equals(name, true) } as Value<T>

    override fun load() {}

    override fun unload() {}

    enum class Category {
        COMBAT
    }
}