package me.chell.samsara.api.value

import com.google.gson.annotations.Expose
import me.chell.samsara.api.util.Globals
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

class Bind(@Expose var key: Int, @Expose var enabled: Boolean = false, @Expose var hold: Boolean = false): Globals {

    constructor(): this(-1)

    private var lastState = enabled

    fun tick(): Int {
        if(hold && key != -1) {
            enabled = if(key < 8) GLFW.glfwGetMouseButton(mc.window.handle, key) == 1 else InputUtil.isKeyPressed(mc.window.handle, key)
            if(lastState && !enabled) {
                lastState = enabled
                return 0 // disable
            }
            if(!lastState && enabled) {
                lastState = enabled
                return 1 // enable
            }
        }
        lastState = enabled
        return -1
    }
}