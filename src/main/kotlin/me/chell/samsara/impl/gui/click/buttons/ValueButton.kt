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
        var height = 12.0

        var border = Border(2.0, 0.0, 0.0, 1.0)
        var border_color = Color(15, 15, 15, 200)
    }

    init {
        openHeight = height
    }

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        val rect = Rectangle(x, y, Window.width, height).subtract(border)
        rect.x += Window.padding.left + Button.border.left
        rect.width -= Window.padding.left + Window.padding.right + Button.border.left + Button.border.right

        // draw background
        fill(matrices, rect, secondary_color)
        //draw border
        drawBorder(matrices, border, rect, border_color)
        // draw text
        drawString(matrices, value.displayName, primary_text, rect)
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

    fun isVisible(): Boolean = value.visible.test(false)
}