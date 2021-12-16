package me.chell.samsara.api.module

import me.chell.samsara.api.Loadable
import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.event.EventManager
import me.chell.samsara.api.util.Wrapper
import me.chell.samsara.impl.event.KeyInputEvent
import me.chell.samsara.impl.module.combat.*
import me.chell.samsara.impl.module.misc.FastUse
import me.chell.samsara.impl.module.movement.*
import me.chell.samsara.impl.module.render.*
import org.lwjgl.glfw.GLFW

object ModuleManager: Loadable, Wrapper {
    val modules = mutableListOf<Module>()

    fun <T> getModule(name: String): T = modules.firstOrNull { it.name.equals(name, true) } as T

    override fun load() {
        EventManager.register(this)
        modules.add(ClickGuiModule())
        modules.add(Sprint())
        modules.add(PullDown())
        modules.add(FastUse())
        modules.add(KillAura())
        modules.add(NoRender())
        modules.add(FullBright())
        modules.add(Velocity())
        for(m in modules) m.load()
    }

    override fun unload() {
        EventManager.unregister(this)
        for(m in modules) m.unload()
        modules.clear()
    }

    @EventHandler
    fun onKeyInput(event: KeyInputEvent) {
        if(event.action == GLFW.GLFW_PRESS && event.key != 0 && mc.currentScreen == null)
            for(m in modules)
                if(m.bind.value.key == event.key) m.toggle()
    }
}