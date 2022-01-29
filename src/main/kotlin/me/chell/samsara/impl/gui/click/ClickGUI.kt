package me.chell.samsara.impl.gui.click

import me.chell.samsara.Samsara
import me.chell.samsara.api.feature.Module
import me.chell.samsara.api.feature.FeatureManager
import me.chell.samsara.api.util.LuaUtils
import me.chell.samsara.impl.gui.click.buttons.FeatureButton
import me.chell.samsara.impl.gui.click.buttons.ValueButton
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.lwjgl.glfw.GLFW
import java.io.File
import java.lang.Exception
import java.nio.file.Path
import java.util.*

class ClickGUI: Screen(LiteralText("ClickGUI")) {

    companion object {
        lateinit var INSTANCE: ClickGUI
    }

    private val windows = mutableListOf<Window>()

    init {
        INSTANCE = this

        windows.clear()

        var x = 10.0
        val y = 20.0
        var buttonY: Double

        // Create category windows
        for(c in Module.Category.values()) {
            val capitalizedName = c.name.lowercase().replaceFirstChar { it.titlecase(Locale.getDefault()) }
            val w = Window(capitalizedName, x, y)
            buttonY = w.y + Window.titleHeight

            for(m in FeatureManager.modules) {
                if(m.category == c)  {
                    val b = FeatureButton(m, x, buttonY)
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

        for(widget in FeatureManager.widgets) {
            val b = FeatureButton(widget, x, buttonY)
            widgetWindow.buttons.add(b)
            buttonY += b.openHeight
        }
        windows.add(widgetWindow)
        x += Window.width + 10.0

        // Create Waypoints window
        val waypointWindow = Window("Waypoints", x, y)
        buttonY = waypointWindow.y + Window.titleHeight
        /*
        for(waypoint in WaypointManager.waypoints) {
            val b = FeatureButton(waypoint, x, buttonY)
            waypointWindow.buttons.add(b)
            buttonY += b.openHeight
        }
        */
        windows.add(waypointWindow)
        x += Window.width + 10.0

        // Create Client window
        val clientWindow = Window("Client", x, y)
        buttonY = clientWindow.y + Window.titleHeight
        for(v in Samsara.values) {
            val b = FeatureButton.createButton(v, x, buttonY)
            clientWindow.buttons.add(b)
            buttonY += b.openHeight
        }
        windows.add(clientWindow)
        x += Window.width + 10.0

        // load the current theme
        loadTheme(Samsara.themeFile.value)
    }

    fun loadTheme(file: File) {
        try {
            LuaUtils.loadFile(file.absolutePath)

            val windowLua = CoerceJavaToLua.coerce(Window)
            val buttonLua = CoerceJavaToLua.coerce(Button)
            val valueLua = CoerceJavaToLua.coerce(ValueButton)

            LuaUtils.globals.get(file.name.dropLast(".lua".length)).call(windowLua, buttonLua, valueLua)

            for(window in windows) {
                window.themeLoaded()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        val m = matrices ?: MatrixStack()

        for(window in windows) {
            window.render(m, mouseX.toDouble(), mouseY.toDouble(), delta)
        }
        for(widget in FeatureManager.widgets) {
            if(!widget.isEnabled()) continue
            widget.render(m, mouseX.toDouble(), mouseY.toDouble(), delta)
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
        for(widget in FeatureManager.widgets) {
            if(!widget.isEnabled()) continue
            if(widget.mouseClicked(mouseX, mouseY, button)) return true
        }
        return false
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        for(window in windows) {
            if(window.mouseReleased(mouseX, mouseY, button)) return true
        }
        for(widget in FeatureManager.widgets) {
            if(!widget.isEnabled()) continue
            if(widget.mouseReleased(mouseX, mouseY, button)) return true
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