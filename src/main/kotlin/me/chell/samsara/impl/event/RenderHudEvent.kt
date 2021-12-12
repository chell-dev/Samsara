package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event
import net.minecraft.client.util.math.MatrixStack

open class RenderHudEvent(open var canceled: Boolean = false): Event() {

    class Post(val matrices: MatrixStack, val tickDelta: Float): RenderHudEvent()
    class Pumpkin(override var canceled: Boolean): RenderHudEvent()
    class Portal(override var canceled: Boolean): RenderHudEvent()
    class Crosshair(val matrices: MatrixStack, override var canceled: Boolean): RenderHudEvent()
    class StatusEffect(val matrices: MatrixStack, override var canceled: Boolean): RenderHudEvent()

    fun cancel() {
        canceled = true
    }

}