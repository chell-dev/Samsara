package me.chell.samsara.impl.module.render

import me.chell.samsara.Samsara
import me.chell.samsara.api.module.Module
import org.lwjgl.glfw.GLFW

class ClickGuiModule: Module("ClickGUI", Category.RENDER) {

    init {
        bind.value.key = GLFW.GLFW_KEY_BACKSLASH
    }

    override fun onEnable() {
        if(mc.currentScreen == null)
            mc.setScreen(Samsara.clickGUI)
        toggle()
    }

    override fun onDisable() {}
}