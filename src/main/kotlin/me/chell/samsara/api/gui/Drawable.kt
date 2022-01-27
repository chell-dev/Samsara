package me.chell.samsara.api.gui

import me.chell.samsara.api.util.Align
import me.chell.samsara.api.util.Color
import me.chell.samsara.api.util.Globals
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack

interface Drawable: Globals {

    fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float)
    fun tick()

    fun guiClosed()
    fun screenResized()
    fun themeLoaded()

    fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean
    fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean
    fun charTyped(char: Char, modifiers: Int): Boolean

    fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean
    fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean
    fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean

    fun fill(matrices: MatrixStack, rect: Rectangle, color: Color) {
        DrawableHelper.fill(matrices, rect.x.toInt(), rect.y.toInt(), rect.endX.toInt(), rect.endY.toInt(), color.argb)
    }

    fun drawBorder(matrices: MatrixStack, border: Border, rect: Rectangle, color: Color) { // draw on the outside of rectangle
        if(border.top != 0.0) {
            val top = Rectangle(rect.x, rect.y - border.top, rect.width, border.top)
            fill(matrices, top, color)
        }
        if(border.bottom != 0.0) {
            val bottom = Rectangle(rect.x, rect.endY, rect.width, border.bottom)
            fill(matrices, bottom, color)
        }
        if(border.left != 0.0) {
            val left = Rectangle(rect.x - border.left, rect.y - border.top, border.left, rect.height + border.top + border.bottom)
            fill(matrices, left, color)
        }
        if(border.right != 0.0) {
            val right = Rectangle(rect.x + rect.width, rect.y - border.top, border.right, rect.height + border.top + border.bottom)
            fill(matrices, right, color)
        }
    }

    fun drawString(matrices: MatrixStack, text: String, properties: TextProperties, container: Rectangle) {
        val bounds = container.subtract(properties.padding)

        val x = when(properties.horizontal) {
            Align.Horizontal.LEFT -> bounds.x
            Align.Horizontal.RIGHT -> bounds.endX - fontRenderer.getWidth(text)
            Align.Horizontal.CENTER -> bounds.x + bounds.width / 2 - fontRenderer.getWidth(text) / 2
        }

        val y = when(properties.vertical) {
            Align.Vertical.TOP -> bounds.y
            Align.Vertical.BOTTOM-> bounds.endY - fontRenderer.fontHeight
            Align.Vertical.CENTER -> bounds.y + fontRenderer.fontHeight / 4
        }

        if(properties.shadow)
            fontRenderer.drawWithShadow(matrices, text, x.toFloat(), y.toFloat(), properties.color.argb)
        else
            fontRenderer.draw(matrices, text, x.toFloat(), y.toFloat(), properties.color.argb)
    }
}