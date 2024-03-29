package me.chell.samsara.impl.gui.click

import me.chell.samsara.api.gui.Border
import me.chell.samsara.api.gui.Drawable
import me.chell.samsara.api.gui.TextProperties
import me.chell.samsara.api.util.Align
import me.chell.samsara.api.util.Color

abstract class Button(open var name: String, open var x: Double, open var y: Double): Drawable {

    var open = false
    var openHeight = height

    companion object {
        @JvmStatic var height = 13.0

        @JvmStatic var primaryColor = Color(100, 0, 150, 200)
        @JvmStatic var secondaryColor = Color(30, 30, 30, 200)

        @JvmStatic var border = Border(0.0, 0.0, 0.0, 1.0)
        @JvmStatic var borderColor = Color(15, 15, 15, 200)

        @JvmStatic var primaryText = TextProperties(Border(1.0, 0.0, 0.0, 0.0), Color(-1), true, Align.Horizontal.LEFT, Align.Vertical.CENTER)
        @JvmStatic var secondaryText = TextProperties(Border(0.0, 2.0, 0.0, 0.0), Color(120, 120, 120, 255), true, Align.Horizontal.RIGHT, Align.Vertical.CENTER)

        @JvmStatic var tooltipBackground = Color(-1)
        @JvmStatic var tooltipBorder = Border()
        @JvmStatic var tooltipBorder_color = Color(-1)
        @JvmStatic var tooltipText = TextProperties(Border(), Color(-1), true, Align.Horizontal.LEFT, Align.Vertical.CENTER)

        @JvmStatic
        fun setBorder(x: Double, y: Double, w: Double, h: Double) {
            border = Border(x, y, w, h)
        }

        @JvmStatic
        fun setBorderColor(r: Int, g: Int, b: Int, a: Int) {
            borderColor = Color(r, g, b, a)
        }

        @JvmStatic
        fun setPrimaryColor(r: Int, g: Int, b: Int, a: Int) {
            primaryColor = Color(r, g, b, a)
        }

        @JvmStatic
        fun setSecondaryColor(r: Int, g: Int, b: Int, a: Int) {
            secondaryColor = Color(r, g, b, a)
        }
    }

    open fun isVisible() = true
}