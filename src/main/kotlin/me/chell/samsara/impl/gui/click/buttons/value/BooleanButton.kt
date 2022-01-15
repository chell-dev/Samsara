package me.chell.samsara.impl.gui.click.buttons.value

import me.chell.samsara.api.gui.Rectangle
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.gui.click.Button
import me.chell.samsara.impl.gui.click.Window
import me.chell.samsara.impl.gui.click.buttons.ValueButton
import net.minecraft.client.util.math.MatrixStack

class BooleanButton(val v: Value<Boolean>, override var x: Double, override var y: Double): ValueButton<Boolean>(v, x, y) {

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        val rect = Rectangle(x, y, Window.width, height).subtract(border)
        rect.x += Window.padding.left + Button.border.left
        rect.width -= Window.padding.left + Window.padding.right + Button.border.left + Button.border.right

        // draw background
        fill(matrices, rect, if(value.value) primaryColor else secondaryColor)
        //draw border
        drawBorder(matrices, border, rect, borderColor)
        // draw text
        drawString(matrices, value.displayName, primaryText, rect)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(Rectangle(x, y, Window.width, height).isInBounds(mouseX, mouseY) && button == 0) {
            value.value = !value.value // lol
            return true
        }
        return false
    }
}