package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event
import net.minecraft.client.util.math.MatrixStack

open class RenderHudEvent: Event.Cancelable() {

    class Post(val matrices: MatrixStack, val tickDelta: Float): RenderHudEvent()
    class Pumpkin: RenderHudEvent()
    class Portal: RenderHudEvent()
    class Crosshair(val matrices: MatrixStack): RenderHudEvent()
    class StatusEffect(val matrices: MatrixStack): RenderHudEvent()

}