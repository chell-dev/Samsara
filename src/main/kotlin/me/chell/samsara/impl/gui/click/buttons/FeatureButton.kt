package me.chell.samsara.impl.gui.click.buttons

import me.chell.samsara.api.feature.Feature
import me.chell.samsara.api.gui.Rectangle
import me.chell.samsara.api.value.Bind
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.gui.click.Button
import me.chell.samsara.impl.gui.click.Window
import me.chell.samsara.impl.gui.click.buttons.value.*
import net.minecraft.client.util.math.MatrixStack
import java.io.File

class FeatureButton(val feature: Feature, override var x: Double, override var y: Double): Button(feature.name, x, y) {

    val valueButtons = mutableListOf<ValueButton<*>>()

    init {
        addButtons()
    }

    @Suppress("unchecked_cast")
    companion object {
        fun createButton(v: Value<*>, x: Double, y: Double): ValueButton<*> {
                return when(v.value) {
                    is Boolean -> BooleanButton(v as Value<Boolean>, x, y)
                    is Enum<*> -> EnumButton(v as Value<Enum<*>>, x, y)
                    is Bind -> BindButton(v as Value<Bind>, x, y)
                    is File -> FileButton(v as Value<File>, x, y)
                    is Runnable -> RunnableButton(v as Value<Runnable>, x, y)
                    is Number -> Slider(v as Value<Number>, x, y)
                    is Char -> CharButton(v as Value<Char>, x, y)
                    else -> ValueButton(v as Value<Any>, x, y)
                }
        }
    }

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        openHeight = height
        val rect = Rectangle(x, y, Window.width, openHeight).subtract(border)
        rect.x += Window.padding.left
        rect.width -= Window.padding.left + Window.padding.right
        //if(open) rect.height += border.bottom

        // draw background
        fill(matrices, rect, if(feature.isEnabled()) primaryColor else secondaryColor)
        // draw text
        drawString(matrices, feature.getDisplayName(), primaryText, rect)

        // draw buttons
        if(open) {
            for (button in valueButtons) {
                if(!button.isVisible()) continue
                button.x = x
                button.y = y + openHeight
                button.render(matrices, mouseX, mouseY, tickDelta)
                openHeight += button.openHeight
            }
        }

        // draw border
        //rect.height += openHeight - height
        drawBorder(matrices, border, rect, borderColor)
    }

    @Suppress("unchecked_cast")
    private fun addButtons() {
        var buttonY = y
        for(v in feature.values) {
            val b = createButton(v, x, buttonY)
            buttonY += b.openHeight
            valueButtons.add(b)
        }
    }

    override fun tick() {
        if(!open) return
        for (button in valueButtons) {
            if(button.isVisible())
                button.tick()
        }
    }

    override fun guiClosed() {
        if(!open) return
        for (button in valueButtons) {
            if(button.isVisible())
                button.guiClosed()
        }
    }

    override fun screenResized() {
        if(!open) return
        for (button in valueButtons) {
            if(button.isVisible())
                button.screenResized()
        }
    }

    override fun themeLoaded() {
        for (button in valueButtons) {
            button.themeLoaded()
        }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if(!open) return false
        for (button in valueButtons) {
            if(button.isVisible())
                if(button.keyPressed(keyCode, scanCode, modifiers)) return true
        }
        return false
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if(!open) return false
        for (button in valueButtons) {
            if(button.isVisible())
                if(button.keyReleased(keyCode, scanCode, modifiers)) return true
        }
        return false
    }

    override fun charTyped(char: Char, modifiers: Int): Boolean {
        if(!open) return false
        for (button in valueButtons) {
            if(button.isVisible())
                if(button.charTyped(char, modifiers)) return true
        }
        return false
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(Rectangle(x, y, Window.width, height).isInBounds(mouseX, mouseY)) {
            when(button) {
                0 -> feature.toggle()
                1 -> open = !open
            }
            return true
        }

        if(!open) return false
        for (b in valueButtons) {
            if(b.isVisible())
                if(b.mouseClicked(mouseX, mouseY, button)) return true
        }

        return false
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(!open) return false
        for (b in valueButtons) {
            if(b.isVisible())
                if(b.mouseReleased(mouseX, mouseY, button)) return true
        }
        return false
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if(!open) return false
        for (b in valueButtons) {
            if(b.isVisible())
                if(b.mouseScrolled(mouseX, mouseY, amount)) return true
        }
        return false
    }
}