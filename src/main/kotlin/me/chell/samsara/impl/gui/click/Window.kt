package me.chell.samsara.impl.gui.click

import me.chell.samsara.api.gui.Border
import me.chell.samsara.api.gui.Drawable
import me.chell.samsara.api.gui.Rectangle
import me.chell.samsara.api.gui.TextProperties
import me.chell.samsara.api.util.Align
import me.chell.samsara.api.util.Color
import net.minecraft.client.util.math.MatrixStack

open class Window(var name: String, var x: Double, var y: Double): Drawable {

    companion object {
        @JvmStatic var width = 110.0

        @JvmStatic var padding = Border(1.0, 0.0, 1.0, 0.0)
        @JvmStatic var paddingColor = Color(15, 15, 15, 200)

        @JvmStatic var titleHeight = 14.0
        @JvmStatic var titleColor = Color(100, 0, 150, 255)

        @JvmStatic var text = TextProperties(Border(2.0, 0.0, 0.0, 0.0), Color(0xffffff), true, Align.Horizontal.LEFT, Align.Vertical.CENTER)

        @JvmStatic var border = Border(2.0, 2.0, 2.0, 2.0)
        @JvmStatic var borderColor = Color(100, 0, 150, 255)
    }

    val buttons = mutableListOf<Button>()
    var open = true

    var grabbed = false
    var moveX = 0.0
    var moveY = 0.0

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        if(grabbed) {
            x = mouseX - moveX
            y = mouseY - moveY
        }

        val t = Rectangle(x, y, width, titleHeight)

        // draw title
        fill(matrices, t, titleColor)
        drawString(matrices, name, text, t)

        var height = titleHeight

        // draw buttons
        if(open) {
            height += padding.top

            for (button in buttons) {
                if(!button.isVisible()) continue
                button.x = x
                button.y = y + height
                button.render(matrices, mouseX, mouseY, tickDelta)
                height += button.openHeight
            }

            height += padding.bottom

            // draw padding
            drawBorder(matrices, padding, Rectangle(x, y + titleHeight, width, height - titleHeight).subtract(padding), paddingColor)
        }

        // draw window border
        drawBorder(matrices, border, Rectangle(x, y, width, height), borderColor)
    }

    override fun tick() {
        if(!open) return
        for(button in buttons) {
            if(!button.isVisible()) continue
            button.tick()
        }
    }

    override fun guiClosed() {
        if(!open) return
        for(button in buttons) {
            if(!button.isVisible()) continue
            button.guiClosed()
        }
    }

    override fun screenResized() {
        if(!open) return
        for(button in buttons) {
            if(!button.isVisible()) continue
            button.screenResized()
        }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if(!open) return false
        for(button in buttons) {
            if(!button.isVisible()) continue
            if(button.keyPressed(keyCode, scanCode, modifiers)) return true
        }
        return false
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if(!open) return false
        for(button in buttons) {
            if(!button.isVisible()) continue
            if(button.keyReleased(keyCode, scanCode, modifiers)) return true
        }
        return false
    }

    override fun charTyped(char: Char, modifiers: Int): Boolean {
        if(!open) return false
        for(button in buttons) {
            if(!button.isVisible()) continue
            if(button.charTyped(char, modifiers)) return true
        }
        return false
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(Rectangle(x, y, width, titleHeight).isInBounds(mouseX, mouseY)) {
            when(button) {
                0 -> {
                    grabbed = true
                    moveX = mouseX - x
                    moveY = mouseY - y
                }
                1 -> open = !open
            }
            return true
        }

        if(!open) return false
        for(b in buttons) {
            if(!b.isVisible()) continue
            if(b.mouseClicked(mouseX, mouseY, button)) return true
        }
        return false
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(grabbed) {
            grabbed = false
            return true
        }

        if(!open) return false
        for(b in buttons) {
            if(!b.isVisible()) continue
            if(b.mouseReleased(mouseX, mouseY, button)) return true
        }
        return false
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if(!open) return false
        for(b in buttons) {
            if(!b.isVisible()) continue
            if(b.mouseScrolled(mouseX, mouseY, amount)) return true
        }
        return false
    }
}