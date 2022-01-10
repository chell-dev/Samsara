package me.chell.samsara.api.gui

import me.chell.samsara.api.util.Color
import me.chell.samsara.api.util.Globals
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

    fun fill(matrices: MatrixStack, rectangle: Rectangle, color: Color) {
    }

    fun drawBorder(matrices: MatrixStack, border: Border, rectangle: Rectangle, color: Color) { // draw on the outside of rectangle
    }

    fun drawString(matrices: MatrixStack, text: String, properties: TextProperties, container: Rectangle) {
        //fontRenderer.drawWithShadow(matrices, text, bounds.x.toFloat(), bounds.y.toFloat(), color.argb)
    }
}