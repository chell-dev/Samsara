package me.chell.samsara.api.value

import me.chell.samsara.api.util.Wrapper
import net.minecraft.client.util.InputUtil

class Bind(var key: Int, var enabled: Boolean = false, var hold: Boolean = false): Wrapper {
    private var lastState = enabled

    fun tick(): Int {
        if(hold) {
            enabled = InputUtil.isKeyPressed(mc.window.handle, key)
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