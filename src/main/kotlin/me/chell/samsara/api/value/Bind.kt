package me.chell.samsara.api.value

import com.google.gson.annotations.Expose
import me.chell.samsara.api.util.Wrapper
import net.minecraft.client.util.InputUtil

class Bind(@Expose var key: Int, @Expose var enabled: Boolean = false, @Expose var hold: Boolean = false): Wrapper {

    constructor(): this(0)

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