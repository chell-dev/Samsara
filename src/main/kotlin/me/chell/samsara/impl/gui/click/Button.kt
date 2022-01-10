package me.chell.samsara.impl.gui.click

import me.chell.samsara.api.gui.Border
import me.chell.samsara.api.gui.Drawable
import me.chell.samsara.api.gui.TextProperties
import me.chell.samsara.api.util.Align
import me.chell.samsara.api.util.Color

abstract class Button(open var name: String, open var x: Double, open var y: Double): Drawable() {

    var open = false
    var openHeight = height

    companion object {
        var height = 10.0

        var primary_color = Color(100, 0, 150, 200)
        var secondary_color = Color(30, 30, 30, 20)

        var border = Border()
        var border_color = Color(-1)

        var primary_text = TextProperties(Border(), Color(-1), true, Align.Horizontal.LEFT)
        var secondary_text = TextProperties(Border(), Color(-1), true, Align.Horizontal.LEFT)

        var tooltip_background = Color(-1)
        var tooltip_border = Border()
        var tooltip_border_color = Color(-1)
        var tooltip_text = TextProperties(Border(), Color(-1), true, Align.Horizontal.LEFT)
    }
}