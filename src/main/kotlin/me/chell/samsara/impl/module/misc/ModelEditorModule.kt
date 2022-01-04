package me.chell.samsara.impl.module.misc

import me.chell.samsara.api.animation.ModelEditor
import me.chell.samsara.api.module.Module

class ModelEditorModule: Module("ModelEditor", Category.MISC, "Opens the model editor") {

    override fun onEnable() {
        mc.setScreen(ModelEditor())
        toggle()
    }

    override fun onDisable() {}
}