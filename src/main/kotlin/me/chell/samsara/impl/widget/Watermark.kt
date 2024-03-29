package me.chell.samsara.impl.widget

import me.chell.samsara.api.feature.Widget
import me.chell.samsara.impl.gui.click.Button
import net.minecraft.client.util.math.MatrixStack

class Watermark: Widget("Watermark") {

    override fun load() {
        super.load()

        width = fontRenderer.getWidth("$MODNAME $MODVER").toDouble()
        height = fontRenderer.fontHeight.toDouble()
    }

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        super.render(matrices, mouseX, mouseY, tickDelta)
        if(mouseX < 0 && !pinned.value) return

        fontRenderer.drawWithShadow(matrices, MODNAME, x.value.toFloat(), y.value.toFloat(), Button.primaryText.color.argb)
        fontRenderer.drawWithShadow(matrices, MODVER, x.value.toFloat() + fontRenderer.getWidth("$MODNAME "), y.value.toFloat(), Button.secondaryText.color.argb)
    }
}