package me.chell.samsara.api.module

import me.chell.samsara.api.Loadable

class ModuleManager: Loadable {
    val modules = mutableListOf<Module>()

    fun <T> getModule(name: String): T = modules.firstOrNull { it.name.equals(name, true) } as T

    override fun load() {
        for(m in modules) m.load()
    }

    override fun unload() {
        for(m in modules) m.unload()
        modules.clear()
    }
}