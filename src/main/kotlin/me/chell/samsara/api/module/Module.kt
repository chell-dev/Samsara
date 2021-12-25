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

    @Register(99) val displayName: Value<String> = ValueBuilder("Display Name", name).visible{false}.build()
    @Register(98) val bind: Value<Bind> = Value("Bind", Bind(-1))

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
        val toAdd = mutableMapOf<Value<*>, Int>()

        for(p in this::class.memberProperties) {
            for(a in p.annotations) {
                if(a is Register) {
                    val v = p.getter.call(this) as Value<*>
                    toAdd[v] = a.order
                }
            }
        }

        for(v in toAdd.keys) values.add(v)
        values.sortBy { toAdd[it] }
    }

    enum class Category {
        COMBAT, RENDER, MOVEMENT, MISC
    }
}