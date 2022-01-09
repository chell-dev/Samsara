package me.chell.samsara.api.util

import org.luaj.vm2.Globals
import org.luaj.vm2.lib.jse.JsePlatform

object LuaUtils {

    val globals: Globals = JsePlatform.standardGlobals()
    
    fun run(lua: String) {
        globals.load(lua).call()
    }

    fun runScript(filePath: String) {
        globals.loadfile(filePath).call()
    }

}