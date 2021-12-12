package me.chell.samsara.api.module

import me.chell.samsara.api.Loadable
import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.event.EventManager
import me.chell.samsara.impl.event.KeyInputEvent
import org.lwjgl.glfw.GLFW

object ModuleManager: Loadable {
    val modules = mutableListOf<Module>()

    fun <T> getModule(name: String): T = modules.firstOrNull { it.name.equals(name, true) } as T

    override fun load() {
        EventManager.register(this)
        for(m in modules) m.load()
    }

    override fun unload() {
        EventManager.unregister(this)
        for(m in modules) m.unload()
        modules.clear()
    }

    @EventHandler
    fun onKeyInput(event: KeyInputEvent) {
        if(event.action == GLFW.GLFW_PRESS && event.key != 0)
            for(m in modules)
                if(m.bind.value.key == event.key) m.toggle()
    }
}