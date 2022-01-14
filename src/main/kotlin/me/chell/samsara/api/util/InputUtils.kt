package me.chell.samsara.api.util

import org.lwjgl.glfw.GLFW

object InputUtils: Globals {

    private val buttons = mutableMapOf<Int, String>()

     init {
         val blacklist = listOf("GLFW_MOUSE_BUTTON_LAST", "GLFW_MOUSE_BUTTON_LEFT", "GLFW_MOUSE_BUTTON_RIGHT", "GLFW_MOUSE_BUTTON_MIDDLE")

         for(field in GLFW::class.java.declaredFields) {

             if(field.name.startsWith("GLFW_KEY_")) {
                 buttons[field.getInt(null)] = field.name.substring("GLFW_KEY_".length)
             }

             else if(field.name.startsWith("GLFW_MOUSE_BUTTON_") && !blacklist.contains(field.name)) {
                 val name = field.name.substring("GLFW_MOUSE_BUTTON_".length)
                 buttons[field.getInt(null)] = "M$name"
             }

         }
     }

    fun getKey(name: String): Int {
        for(key in buttons.keys) {
            if(buttons[key].equals(name, true)) return key
        }

        return -1
    }

    fun getName(keyCode: Int): String? = buttons.getOrDefault(keyCode, null)

}