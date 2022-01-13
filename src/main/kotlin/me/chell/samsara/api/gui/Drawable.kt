package me.chell.samsara.api.gui

import me.chell.samsara.api.util.Color
import me.chell.samsara.api.util.Globals
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack

abstract class Drawable: Globals {

    abstract fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float)
    abstract fun tick()

    abstract fun guiClosed()
    abstract fun screenResized()

    abstract fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean
    abstract fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean
    abstract fun charTyped(char: Char, modifiers: Int): Boolean

    abstract fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean
    abstract fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean
    abstract fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean

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
        // x, y, color
        fontRenderer.drawWithShadow(matrices, text, container.x.toFloat(), container.y.toFloat(), -1)
    }
}