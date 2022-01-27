package me.chell.samsara.impl.gui.click.buttons.value

import me.chell.samsara.api.gui.Rectangle
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.gui.click.Button
import me.chell.samsara.impl.gui.click.Window
import me.chell.samsara.impl.gui.click.buttons.ValueButton
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import org.lwjgl.glfw.GLFW
import java.lang.NumberFormatException
import kotlin.math.roundToInt

class Slider(val v: Value<Number>, override var x: Double, override var y: Double): ValueButton<Number>(v, x, y) {

    private var grabbed = false
    private var sliderWidth = 0.0

    private var listening = false
    private var input = ""
    private var tickCounter = 0

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        val rect = Rectangle(x, y, Window.width, height).subtract(border)
        rect.x += Window.padding.left + Button.border.left
        rect.width -= Window.padding.left + Window.padding.right + Button.border.left + Button.border.right

        if (grabbed) {
            val mousePos = MathHelper.clamp(mouseX - rect.x, 0.0, rect.width)

            val percent = mousePos / rect.width

            val v = (value.max!!.toDouble() - value.min!!.toDouble()) * percent + value.min.toDouble()

            setValue((v * 100.0).roundToInt() / 100.0)
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

        var text = if(listening) input else value.value.toString()
        if(listening && tickCounter % 10 > 3) text += '\u007C'

        drawString(matrices, text, secondaryText, rect)
    }

    override fun tick() {
        tickCounter++
    }

    override fun screenResized() {
        setSliderWidth()
        tickCounter = 0
    }

    override fun themeLoaded() {
        super.themeLoaded()
        if(isVisible())
            setSliderWidth()
    }

    override fun guiClosed() {
        listening = false
        grabbed = false
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(Rectangle(x, y, Window.width, height).isInBounds(mouseX, mouseY)) {
            when(button) {
                0 -> {
                    grabbed = true
                    return true
                }
                1 -> {
                    listening = true
                    input = value.value.toString()
                    return true
                }
            }
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

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if(!listening) return false

        return when(keyCode) {
            GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                try {
                    when(value.min) {
                        is Int -> setValue(input.toInt())
                        is Double -> setValue(input.toDouble())
                        is Float -> setValue(input.toFloat())
                    }
                } catch (ignored: NumberFormatException) {}

                listening = false
                true
            }
            GLFW.GLFW_KEY_BACKSPACE -> {
                input = input.dropLast(1)
                true
            }
            GLFW.GLFW_KEY_ESCAPE -> {
                listening = false
                true
            }
            else -> false
        }
    }

    override fun charTyped(char: Char, modifiers: Int): Boolean {
        if(listening && whitelist.contains(char)) {
            input += char
            return true
        }
        return false
    }

    private fun setSliderWidth() {
        val min = value.min!!.toDouble()
        val max = value.max!!.toDouble()
        val v = value.value.toDouble()
        val diff = max - min

        val percent = (v - min) / diff
        val width = Window.width - Window.padding.left - Window.padding.right - Button.border.left - Button.border.right - border.left - border.right

        sliderWidth = MathHelper.clamp(width * percent, 0.0, width)
    }

    private fun setValue(v: Number) {
        value.value = if(value.min is Int) v.toInt() else v
        setSliderWidth()
    }
}