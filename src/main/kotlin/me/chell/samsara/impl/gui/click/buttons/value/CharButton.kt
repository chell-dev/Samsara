package me.chell.samsara.impl.gui.click.buttons.value

import me.chell.samsara.api.gui.Rectangle
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.gui.click.Button
import me.chell.samsara.impl.gui.click.Window
import me.chell.samsara.impl.gui.click.buttons.ValueButton
import net.minecraft.client.util.math.MatrixStack

class CharButton(val v: Value<Char>, override var x: Double, override var y: Double): ValueButton<Char>(v, x, y) {

    private var listening = false

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        val rect = Rectangle(x, y, Window.width, height).subtract(border)
        rect.x += Window.padding.left + Button.border.left
        rect.width -= Window.padding.left + Window.padding.right + Button.border.left + Button.border.right

        // draw background
        fill(matrices, rect, if(listening) primaryColor else secondaryColor)
        //draw border
        drawBorder(matrices, border, rect, borderColor)
        // draw text
        drawString(matrices, value.displayName, primaryText, rect)

        val w = fontRenderer.getWidth(value.displayName)
        rect.x += w
        rect.width -= w

        drawString(matrices, value.value.toString(), secondaryText, rect)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(Rectangle(x, y, Window.width, height).isInBounds(mouseX, mouseY) && button == 0) {
            listening = !listening
            return true
        }

        return false
    }

    override fun charTyped(char: Char, modifiers: Int): Boolean {
        if(listening) {
            value.value = char
            listening = false
            return true
        }
        return false
    }

    override fun guiClosed() {
        listening = false
    }
}