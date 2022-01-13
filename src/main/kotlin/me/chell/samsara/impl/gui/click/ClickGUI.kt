package me.chell.samsara.impl.gui.click

import com.mojang.blaze3d.systems.RenderSystem
import me.chell.samsara.api.module.Module
import me.chell.samsara.api.module.ModuleManager
import me.chell.samsara.impl.gui.click.buttons.FeatureButton
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import org.lwjgl.glfw.GLFW
import java.nio.file.Path
import java.util.*

class ClickGUI: Screen(LiteralText("ClickGUI")) {

    companion object {
        var INSTANCE: ClickGUI? = null
    }

    private val windows = mutableListOf<Window>()

    init {
        windows.clear()

        var x = 10.0
        val y = 20.0
        var buttonY: Double

        // Create category windows
        for(c in Module.Category.values()) {
            val capitalizedName = c.name.lowercase().replaceFirstChar { it.titlecase(Locale.getDefault()) }
            val w = Window(capitalizedName, x, y)
            buttonY = w.y + Window.titleHeight

            for(m in ModuleManager.modules) {
                val b = FeatureButton(m, x, buttonY)
                if(m.category == c)  {
                    w.buttons.add(b)
                    buttonY += b.openHeight
                }
            }

            windows.add(w)
            x += Window.width + 10.0
        }

        // Create Widgets window
        val widgetWindow = Window("Widgets", x, y)
        buttonY = widgetWindow.y + Window.titleHeight

        /*
        for(widget in WidgetManager.widgets) {
            val b = FeatureButton(widget, x, buttonY)
            widgetWindow.buttons.add(b)
            buttonY += b.openHeight
        }
        */
        windows.add(widgetWindow)
        x += Window.width + 10.0

        // Create Client window

        // Create Config window

    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        val m = matrices ?: MatrixStack()

        for(window in windows) {
            window.render(m, mouseX.toDouble(), mouseY.toDouble(), delta)
        }
    }

    override fun tick() {
        for(window in windows) {
            window.tick()
        }
    }

    override fun onClose() {
        for(window in windows) {
            window.guiClosed()
        }
        super.onClose()
    }

    override fun init() {
        for(window in windows) {
            window.screenResized()
        }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        for(window in windows) {
            if(window.keyPressed(keyCode, scanCode, modifiers)) return true
        }
        if(keyCode == GLFW.GLFW_KEY_ESCAPE) {
            onClose()
            return true
        }
        return false
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        for(window in windows) {
            if(window.keyReleased(keyCode, scanCode, modifiers)) return true
        }
        return false
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        for(window in windows) {
            if(window.charTyped(chr, modifiers)) return true
        }
        return false
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        for(window in windows) {
            if(window.mouseClicked(mouseX, mouseY, button)) return true
        }
        return false
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        for(window in windows) {
            if(window.mouseReleased(mouseX, mouseY, button)) return true
        }
        return false
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        for(window in windows) {
            if(window.mouseScrolled(mouseX, mouseY, amount)) return true
        }
        return false
    }



    override fun filesDragged(paths: MutableList<Path>?) {
        super.filesDragged(paths)
    }
    override fun shouldCloseOnEsc(): Boolean = false
    override fun renderBackground(matrices: MatrixStack?) {}
    override fun shouldPause(): Boolean = false
}