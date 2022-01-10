package me.chell.samsara.impl.gui.click.buttons

import me.chell.samsara.api.gui.Border
import me.chell.samsara.api.util.Color
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.gui.click.Button
import net.minecraft.client.util.math.MatrixStack

abstract class ValueButton<T>(val value: Value<T>, override var x: Double, override var y: Double): Button(value.name, x, y) {
    companion object {
        var height = 8.0

        var border = Border()
        var border_color = Color(-1)
    }

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        //  TODO blank button by default
    }

    fun isVisible(): Boolean = value.visible.test(false)
}