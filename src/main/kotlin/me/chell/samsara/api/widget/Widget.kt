package me.chell.samsara.api.widget

import me.chell.samsara.Samsara
import me.chell.samsara.api.Feature
import me.chell.samsara.api.Loadable
import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.event.EventManager
import me.chell.samsara.api.gui.Drawable
import me.chell.samsara.api.gui.Rectangle
import me.chell.samsara.api.util.Globals
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.event.RenderHudEvent
import me.chell.samsara.impl.gui.click.Button
import me.chell.samsara.impl.gui.click.ClickGUI
import net.minecraft.client.util.math.MatrixStack
import kotlin.reflect.full.memberProperties

open class Widget(final override val name: String, val description: String = "No description."): Feature(name), Loadable, Globals, Drawable {

    override val values = mutableListOf<Value<*>>()

    @Register(99) val x = Value("X", 0.0, visible = { false })
    @Register(99) val y = Value("Y", 0.0, visible = { false })
    var width = 1.0
    var height = 1.0

    @Register(98) val pinned = Value("Pinned", true, visible = { true })
    @Register(99) val enabled = Value("Enabled", false, visible = { false })
    @Register(99) val displayName = Value("Display Name", name, visible = { false })

    private var grabbed = false
    var moveX = 0.0
    var moveY = 0.0

    @EventHandler
    fun onRender2D(event: RenderHudEvent.Post) {
        if(mc.currentScreen !is ClickGUI)
            render(event.matrices, -1.0, -1.0, mc.tickDelta)
    }

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        if(mouseX >= 0) {
            fill(matrices, Rectangle(x.value, y.value, width, height), Button.secondaryColor)

            if (grabbed) {
                x.value = mouseX - moveX
                y.value = mouseY - moveY
            }
        }

        if(x.value < 0.0) x.value = 0.0
        if(y.value < 0.0) y.value = 0.0
        if(x.value + width > mc.window.scaledWidth) x.value = mc.window.scaledWidth - width
        if(y.value + height > mc.window.scaledHeight) y.value = mc.window.scaledHeight - height
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(Rectangle(x.value, y.value, width, height).isInBounds(mouseX, mouseY) && button == 0 && !Samsara.lockWidgets.value) {
            grabbed = true
            moveX = mouseX - x.value
            moveY = mouseY - y.value

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

    override fun isEnabled() = enabled.value

    override fun toggle() {
        if(isEnabled()) {
            enabled.value = false
            onDisable()
        } else {
            enabled.value = true
            onEnable()
        }
    }

    override fun getDisplayName() = displayName.value

    open fun onEnable() {
        EventManager.register(this)
    }
    open fun onDisable() {
        EventManager.unregister(this)
    }

    override fun load() {
        registerValues()
    }

    override fun unload() {
        if(isEnabled()) toggle()
        values.clear()
    }

    private fun registerValues() {
        val toAdd = mutableMapOf<Value<*>, Int>()

        for(p in this::class.memberProperties) {
            for(a in p.annotations) {
                if(a is Register) {
                    val v = p.getter.call(this) as Value<*>
                    toAdd[v] = a.order
                }
            }
        }

        for(v in toAdd.keys) values.add(v)
        values.sortBy { toAdd[it] }
    }

    override fun tick() {}

    override fun guiClosed() {}

    override fun screenResized() {}

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int) = false

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int) = false

    override fun charTyped(char: Char, modifiers: Int) = false

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double) = false
}