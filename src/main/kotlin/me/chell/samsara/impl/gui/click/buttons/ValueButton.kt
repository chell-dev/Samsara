package me.chell.samsara.impl.gui.click.buttons

import me.chell.samsara.api.gui.Border
import me.chell.samsara.api.gui.Rectangle
import me.chell.samsara.api.util.Color
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.gui.click.Button
import me.chell.samsara.impl.gui.click.Window
import net.minecraft.client.util.math.MatrixStack

open class ValueButton<T>(val value: Value<T>, override var x: Double, override var y: Double): Button(value.name, x, y) {
    companion object {
        @JvmStatic var height = 12.0

        @JvmStatic var border = Border(2.0, 0.0, 0.0, 1.0)
        @JvmStatic var borderColor = Color(15, 15, 15, 200)

        @JvmStatic
        fun setBorder(x: Double, y: Double, w: Double, h: Double) {
            border = Border(x, y, w, h)
        }

        @JvmStatic
        fun setBorderColor(r: Int, g: Int, b: Int, a: Int) {
            borderColor = Color(r, g, b, a)
        }
    }

    init {
        openHeight = height
    }

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        val rect = Rectangle(x, y, Window.width, height).subtract(border)
        rect.x += Window.padding.left + Button.border.left
        rect.width -= Window.padding.left + Window.padding.right + Button.border.left + Button.border.right

        // draw background
        fill(matrices, rect, secondaryColor)
        //draw border
        drawBorder(matrices, border, rect, borderColor)
        // draw text
        drawString(matrices, value.displayName, primaryText, rect)
    }

    override fun themeLoaded() {
        openHeight = height
    }

    override fun tick() {}

    override fun guiClosed() {}

    override fun screenResized() {}

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = false

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean  = false

    override fun charTyped(char: Char, modifiers: Int): Boolean = false

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean = false

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean = false

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean = false

    override fun isVisible() = value.visible.test(false)
}