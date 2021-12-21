package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event
import net.minecraft.client.util.math.MatrixStack

class RenderEvent(val matrices: MatrixStack, val tickDelta: Float): Event()