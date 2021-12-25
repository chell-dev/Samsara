package me.chell.samsara.api.module

import com.google.gson.annotations.Expose
import me.chell.samsara.api.Loadable
import me.chell.samsara.api.event.EventManager
import me.chell.samsara.api.util.Globals
import me.chell.samsara.api.value.Bind
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.api.value.ValueBuilder
import kotlin.reflect.full.memberProperties

open class Module(
    @Expose val name: String,
    val category: Category,
    val description: String = "No description."
    ): Loadable, Globals {

    constructor() : this("", Category.MISC) // for json, do not use this

    @Expose
    val values = mutableListOf<Value<*>>()

    @Register val displayName: Value<String> = ValueBuilder("Display Name", name).visible{false}.build()
    @Register val bind: Value<Bind> = Value("Bind", Bind(-1))

    fun isEnabled(): Boolean = bind.value.enabled

    fun toggle() {
        if(isEnabled()) {
            bind.value.enabled = false
            onDisable()
        } else {
            bind.value.enabled = true
            onEnable()
        }
    }

    open fun onEnable() {
        EventManager.register(this)
    }
    open fun onDisable() {
        EventManager.unregister(this)
    }

    fun <T> getValue(name: String): Value<T>? = values.firstOrNull { it.name.equals(name, true) } as Value<T>?

    override fun load() {
        registerValues()
    }

    override fun unload() {
        if(isEnabled()) toggle()
        values.clear()
    }

    private fun registerValues() {
        for(p in this::class.memberProperties) {
            if(p.annotations.isNotEmpty()) {
                if(p.annotations[0].annotationClass == Register::class) {
                    val a = p.getter.call(this)
                    values.add(0, a as Value<*>)
                }
            }
        }
    }

    enum class Category {
        COMBAT, RENDER, MOVEMENT, MISC
    }
}