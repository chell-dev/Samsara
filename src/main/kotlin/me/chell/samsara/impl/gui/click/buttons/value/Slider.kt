package me.chell.samsara.impl.gui.click.buttons.value

import me.chell.samsara.api.gui.Rectangle
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.gui.click.Button
import me.chell.samsara.impl.gui.click.Window
import me.chell.samsara.impl.gui.click.buttons.ValueButton
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import org.lwjgl.glfw.GLFW
import kotlin.math.roundToInt

class Slider(val v: Value<Number>, override var x: Double, override var y: Double): ValueButton<Number>(v, x, y) {

    private var grabbed = false
    private var sliderWidth = 0.0

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        setSliderWidth()
        val rect = Rectangle(x, y, Window.width, height).subtract(border)
        rect.x += Window.padding.left + Button.border.left
        rect.width -= Window.padding.left + Window.padding.right + Button.border.left + Button.border.right

        if (grabbed) {
            val mousePos = MathHelper.clamp(mouseX - rect.x, 0.0, rect.width)

            val percent = mousePos / rect.width

            // TODO round int values
            var v = (value.max!!.toDouble() - value.min!!.toDouble()) * percent + value.min.toDouble()
            v = (v * 100.0).roundToInt() / 100.0
            value.value = v
            // value.value = lerp
        }

        //draw border
        drawBorder(matrices, border, rect, borderColor)

        // draw background
        rect.x += sliderWidth
        rect.width -= sliderWidth
        fill(matrices, rect, secondaryColor)

        // draw slider
        if (sliderWidth > 0.0) {
            val sliderRect = Rectangle(rect.x, rect.y, rect.width, rect.height)
            sliderRect.x -= sliderWidth
            sliderRect.width = sliderWidth

            fill(matrices, sliderRect, primaryColor)
        }

        // draw text
        rect.x -= sliderWidth
        rect.width += sliderWidth
        drawString(matrices, value.displayName, primaryText, rect)

        val w = fontRenderer.getWidth(value.displayName)
        rect.x += w
        rect.width -= w

        drawString(matrices, value.value.toString(), secondaryText, rect)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(Rectangle(x, y, Window.width, height).isInBounds(mouseX, mouseY) && button == 0) {
            grabbed = true
            return true
        }
        return false
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(grabbed) {
            grabbed = false
            return true
        }
        return false
    }

    private val whitelist = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-', 'e')

    // TODO keyboard input
    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        when(keyCode) {
            GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
            }
            GLFW.GLFW_KEY_BACKSPACE -> {
            }
            GLFW.GLFW_KEY_ESCAPE -> {
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    private fun setSliderWidth() {
        val min = value.min!!.toDouble()
        val max = value.max!!.toDouble()
        val v = value.value.toDouble()
        val diff = max - min

        var d = v - min
        if(d < 0) d *= -1

        val percent = d / diff
        val width = Window.width - Window.padding.left - Window.padding.right - Button.border.left - Button.border.right - border.left - border.right

        sliderWidth = MathHelper.clamp(width * percent, 0.0, width)
    }
}