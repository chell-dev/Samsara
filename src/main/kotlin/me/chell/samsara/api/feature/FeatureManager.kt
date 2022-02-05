package me.chell.samsara.api.feature

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
import me.chell.samsara.impl.widget.*
import org.lwjgl.glfw.GLFW

object FeatureManager: Loadable, Globals {

    val modules = mutableListOf<Module>()
    val widgets = mutableListOf<Widget>()

    fun <T> getModule(name: String): T? = modules.firstOrNull { it.name.equals(name, true) } as T?

    fun <T> getWidget(name: String): T? = widgets.firstOrNull { it.name.equals(name, true) } as T?

    private fun loadModules() {
        modules.add(Sprint())
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
        modules.add(Bhop())
        modules.add(BetterPingDisplay())
        modules.add(DiscordRPC())
        modules.add(ClickGuiModule())
        modules.add(UwuChat())

        for(m in modules) m.load()
    }

    private fun loadWidgets() {
        widgets.add(Watermark())
        widgets.add(Armor())
        widgets.add(Coordinates())
        widgets.add(Neko())

        for(w in widgets) w.load()
    }

    override fun load() {
        EventManager.register(this)
        loadModules()
        loadWidgets()
    }

    override fun unload() {
        EventManager.unregister(this)

        for(m in modules) m.unload()
        modules.clear()

        for(w in widgets) w.unload()
        widgets.clear()
    }

    // vvv Module Binds vvv

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