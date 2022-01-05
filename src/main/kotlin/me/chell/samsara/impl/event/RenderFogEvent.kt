package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event
import net.minecraft.client.render.BackgroundRenderer

class RenderFogEvent(val fogType: BackgroundRenderer.FogType): Event.Cancelable()