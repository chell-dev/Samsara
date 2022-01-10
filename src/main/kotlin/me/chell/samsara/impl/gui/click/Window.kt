package me.chell.samsara.impl.gui.click

import me.chell.samsara.api.gui.Border
import me.chell.samsara.api.gui.Drawable
import me.chell.samsara.api.gui.Rectangle
import me.chell.samsara.api.gui.TextProperties
import me.chell.samsara.api.util.Align
import me.chell.samsara.api.util.Color
import net.minecraft.client.util.math.MatrixStack

open class Window(var name: String, var x: Double, var y: Double): Drawable() {

    companion object {
        var width = 110.0

        var padding = Border()
        var padding_color = Color(20, 20, 20, 50)

        var title_height = 10.0
        var title_color = Color(100, 0, 150, 255)

        var text = TextProperties(Border(), Color(-1), true, Align.Horizontal.LEFT)

        var border = Border()
        var border_color = Color(-1)
    }

    val buttons = mutableListOf<Button>()
    var open = true
    var grabbed = false

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        val t = Rectangle(x, y, width, title_height)

        fill(matrices, t, title_color)
        drawString(matrices, name, text, t)

        var height = title_height

        if(open) {
            for (button in buttons) {
                button.x = x
                button.y = y + height
                button.render(matrices, mouseX, mouseY, tickDelta)
                height += button.openHeight
            }

            drawBorder(matrices, padding, Rectangle(x, y, width, height).subtract(padding), padding_color)
        }

        drawBorder(matrices, border, Rectangle(x, y, width, height), border_color)
    }

    override fun tick() {
        if(!open) return
        for(button in buttons) {
            button.tick()
        }
    }

    override fun guiClosed() {
        if(!open) return
        for(button in buttons) {
            button.guiClosed()
        }
    }

    override fun screenResized() {
        if(!open) return
        for(button in buttons) {
            button.screenResized()
        }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if(!open) return false
        for(button in buttons) {
            if(button.keyPressed(keyCode, scanCode, modifiers)) return true
        }
        return false
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if(!open) return false
        for(button in buttons) {
            if(button.keyReleased(keyCode, scanCode, modifiers)) return true
        }
        return false
    }

    override fun charTyped(char: Char, modifiers: Int): Boolean {
        if(!open) return false
        for(button in buttons) {
            if(button.charTyped(char, modifiers)) return true
        }
        return false
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(Rectangle(x, y, width, title_height).isInBounds(mouseX, mouseY)) {
            when(button) {
                0 -> grabbed = true
                1 -> open = !open
            }
            return true
        }

        if(!open) return false
        for(b in buttons) {
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
            if(b.mouseReleased(mouseX, mouseY, button)) return true
        }
        return false
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if(!open) return false
        for(b in buttons) {
            if(b.mouseScrolled(mouseX, mouseY, amount)) return true
        }
        return false
    }
}