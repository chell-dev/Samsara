package me.chell.samsara.impl.gui.click.buttons.value

import me.chell.samsara.api.gui.Rectangle
import me.chell.samsara.api.util.InputUtils
import me.chell.samsara.api.value.Bind
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.gui.click.Button
import me.chell.samsara.impl.gui.click.Window
import me.chell.samsara.impl.gui.click.buttons.ValueButton
import net.minecraft.client.util.math.MatrixStack
import org.lwjgl.glfw.GLFW

class BindButton(val v: Value<Bind>, override var x: Double, override var y: Double): ValueButton<Bind>(v, x, y) {

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

        // draw value text
        val w = fontRenderer.getWidth(value.displayName)
        rect.x += w
        rect.width -= w

        var string = if(value.value.hold) "Hold " else ""
        string += InputUtils.getName(value.value.key)
        drawString(matrices, string, secondaryText, rect)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if(listening) {
            when(keyCode) {
                GLFW.GLFW_KEY_ESCAPE -> {
                    listening = false
                    return true
                }
                GLFW.GLFW_KEY_DELETE -> {
                    value.value.key = -1
                    listening = false
                    return true
                }
                else -> {
                    value.value.key = keyCode
                    listening = false
                    return true
                }
            }
        }
        return false
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(Rectangle(x, y, Window.width, height).isInBounds(mouseX, mouseY)) {
            when(button) {
                0 -> {
                    return if(listening) {
                        value.value.key = 0
                        listening = false
                        true
                    } else {
                        listening = true
                        true
                    }
                }
                1 -> {
                    return if (listening) {
                        value.value.key = 1
                        listening = false
                        true
                    } else {
                        value.value.hold = !value.value.hold
                        true
                    }
                }
                else -> {
                    if (listening) {
                        value.value.key = button
                        listening = false
                        return true
                    }
                }
            }
        }
        return false
    }
}