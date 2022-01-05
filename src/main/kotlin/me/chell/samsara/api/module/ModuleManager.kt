package me.chell.samsara.api.module

import me.chell.samsara.api.Loadable
import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.event.EventManager
import me.chell.samsara.api.util.Globals
import me.chell.samsara.api.value.Bind
import me.chell.samsara.impl.event.KeyInputEvent
import me.chell.samsara.impl.event.MouseInputEvent
import me.chell.samsara.impl.event.PlayerTickEvent
import me.chell.samsara.impl.module.combat.*
import me.chell.samsara.impl.module.misc.*
import me.chell.samsara.impl.module.movement.*
import me.chell.samsara.impl.module.render.*
import org.lwjgl.glfw.GLFW

object ModuleManager: Loadable, Globals {
    val modules = mutableListOf<Module>()

    fun <T> getModule(name: String): T? = modules.firstOrNull { it.name.equals(name, true) } as T?

    override fun load() {
        EventManager.register(this)
        modules.add(ClickGuiModule())
        modules.add(Sprint())
        modules.add(PullDown())
        modules.add(FastUse())
        modules.add(KillAura())
        modules.add(NoRender())
        modules.add(Brightness())
        modules.add(Velocity())
        modules.add(Zoom())
        modules.add(FakeVanilla())
        modules.add(AirPlace())
        modules.add(ESP())
        modules.add(PearlBind())
        modules.add(KillEffects())
        modules.add(ModelEditorModule())
        modules.add(ViewModel())
        modules.add(NoFog())
        for(m in modules) m.load()
    }

    override fun unload() {
        EventManager.unregister(this)
        for(m in modules) m.unload()
        modules.clear()
    }

    @EventHandler
    fun onKeyInput(event: KeyInputEvent) {
        if(event.action == GLFW.GLFW_PRESS && event.key != -1 && mc.currentScreen == null)
            for(m in modules)
                if(m.bind.value.key == event.key && !m.bind.value.hold) m.toggle()
    }

    @EventHandler
    fun onMouseInput(event: MouseInputEvent.Button) {
        if(event.action == GLFW.GLFW_PRESS && event.button != -1 && mc.currentScreen == null) {
            for(m in modules)
                if(m.bind.value.key == event.button && !m.bind.value.hold) m.toggle()
        }
    }

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        if(mc.currentScreen != null) return

        for(m in modules) {
            for (v in m.values) {

                val value = v.value
                if (value is Bind) {
                    when (value.tick()) {
                        -1 -> continue
                        0 -> m.onDisable()
                        1 -> m.onEnable()
                    }
                }

            }
        }
    }
}