package me.chell.samsara.impl.widget

import me.chell.samsara.api.feature.Widget
import me.chell.samsara.impl.gui.click.Button
import net.minecraft.client.util.math.MatrixStack
import kotlin.math.roundToInt

class Coordinates: Widget("Coordinates") {

    private var xOffset = 0

    init {
        height = fontRenderer.fontHeight - 1.0
    }

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        super.render(matrices, mouseX, mouseY, tickDelta)
        if(mouseX < 0 && !pinned.value) return

        val primary = Button.primaryText.color.argb
        val secondary = Button.secondaryText.color.argb
        xOffset = 0

        draw(matrices, "XYZ ", secondary)

        draw(matrices, "${player.x.round()}", primary)
        draw(matrices, ", ", secondary)

        draw(matrices, "${player.y.round()}", primary)
        draw(matrices, ", ", secondary)

        draw(matrices, "${player.z.round()}", primary)

        width = xOffset.toDouble()
    }

    private fun draw(matrices: MatrixStack, text: String, color: Int) {
        fontRenderer.drawWithShadow(matrices, text, x.value.toFloat() + xOffset, y.value.toFloat(), color)
        xOffset += fontRenderer.getWidth(text)
    }

    private fun Double.round() = (this * 10).roundToInt() / 10.0
}