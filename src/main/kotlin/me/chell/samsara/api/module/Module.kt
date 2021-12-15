package me.chell.samsara.api.module

import me.chell.samsara.Samsara
import me.chell.samsara.api.Loadable
import me.chell.samsara.api.event.EventManager
import me.chell.samsara.api.util.Wrapper
import me.chell.samsara.api.value.Bind
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.api.value.ValueBuilder
import kotlin.reflect.full.memberProperties

abstract class Module(
    val name: String,
    val category: Category,
    val description: String = "No description."
    ): Loadable, Wrapper {

    val values = mutableListOf<Value<*>>()

    @Register val displayName: Value<String> = ValueBuilder("Display Name", name).visible{false}.build()
    @Register val enabled: Value<Boolean> = ValueBuilder("Enabled", false).visible{false}.build()
    @Register val bind: Value<Bind> = Value("Bind", Bind(0))

    fun toggle() {
        if(enabled.value) {
            enabled.value = false
            onDisable()
        } else {
            enabled.value = true
            onEnable()
        }
    }

    open fun onEnable() {
        EventManager.register(this)
    }
    open fun onDisable() {
        EventManager.unregister(this)
    }

    fun <T> getValue(name: String): Value<T> = values.firstOrNull { it.name.equals(name, true) } as Value<T>

    override fun load() {
        registerValues()
    }

    override fun unload() {
        values.clear()
    }

    private fun registerValues() {
        for(p in this::class.memberProperties) {
            if(p.annotations.isNotEmpty()) {
                if(p.annotations[0].annotationClass == Register::class) {
                    val a = p.getter.call(this)
                    Samsara.LOGGER.info(a)
                    values.add(0, a as Value<*>)
                }
            }
        }
    }

    enum class Category {
        COMBAT, RENDER, MOVEMENT, MISC
    }
}