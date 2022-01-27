package me.chell.samsara.api.gui

import me.chell.samsara.api.util.Align
import me.chell.samsara.api.util.Color

data class TextProperties(var padding: Border, var color: Color, var shadow: Boolean, var horizontal: Align.Horizontal, var vertical: Align.Vertical) {

    fun setPadding(x: Double, y: Double, w: Double, h: Double) {
        padding = Border(x, y, w, h)
    }

    fun setColor(r: Int, g: Int, b: Int, a: Int) {
        color = Color(r, g, b, a)
    }

    fun setHorizontal(value: String) {
        horizontal = Align.Horizontal.valueOf(value)
    }

    fun setVertical(value: String) {
        vertical = Align.Vertical.valueOf(value)
    }
}
