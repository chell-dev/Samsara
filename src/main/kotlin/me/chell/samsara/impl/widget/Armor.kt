package me.chell.samsara.impl.widget

import me.chell.samsara.api.feature.Widget
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import net.minecraft.client.util.math.MatrixStack

class Armor: Widget("Armor") {

    @Register val vertical = Value("Vertical", false)
    @Register val reverse = Value("Reverse", true)

    private var itemSize = 16

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        super.render(matrices, mouseX, mouseY, tickDelta)
        if(mouseX < 0 && !pinned.value) return

        if(vertical.value) {
            height = itemSize*4.0
            width = itemSize.toDouble()
        } else {
            height = itemSize.toDouble()
            width = itemSize*4.0
        }

        var xOffset = 0
        var yOffset = 0

        val list = if(reverse.value) player.armorItems.reversed() else player.armorItems
        for(item in list) {
            mc.itemRenderer.renderGuiItemIcon(item, x.value.toInt() + xOffset, y.value.toInt() + yOffset)
            mc.itemRenderer.renderGuiItemOverlay(fontRenderer, item, x.value.toInt() + xOffset, y.value.toInt() + yOffset)

            if(vertical.value) yOffset += itemSize
            else xOffset += itemSize
        }
    }
}