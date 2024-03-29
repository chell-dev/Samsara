package me.chell.samsara.api.feature

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import me.chell.samsara.api.Loadable
import me.chell.samsara.api.event.EventManager
import me.chell.samsara.api.util.Globals
import me.chell.samsara.api.value.Bind
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import kotlin.reflect.full.memberProperties

open class Module(
    @Expose @SerializedName("module") final override val name: String,
    val category: Category,
    val description: String = "No description."
    ): Feature(name), Loadable, Globals {

    private constructor() : this("", Category.MISC) // for json

    @Expose
    override val values = mutableListOf<Value<*>>()

    @Register(99) val displayName = Value("Display Name", name, visible = { false })
    @Register(98) val bind = Value("Bind", Bind(-1))

    override fun isEnabled(): Boolean = bind.value.enabled

    override fun toggle() {
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

    override fun getDisplayName(): String = displayName.value

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