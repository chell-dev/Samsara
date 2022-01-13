package me.chell.samsara.impl.module.render

import me.chell.samsara.api.module.Module
import me.chell.samsara.impl.gui.click.ClickGUI
import org.lwjgl.glfw.GLFW

class ClickGuiModule: Module("ClickGUI", Category.RENDER, "Opens the ClickGUI.") {

    init {
        bind.value.key = GLFW.GLFW_KEY_BACKSLASH
    }

    override fun onEnable() {
        if(mc.currentScreen == null)
            mc.setScreen(ClickGUI.INSTANCE)
        toggle()
    }

    override fun onDisable() {}
}