package me.chell.samsara.impl.module.render

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.module.Module
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.event.RenderOverlayEvent

class NoRender: Module("NoRender", Category.RENDER) {

    @Register val fire = Value("OnFire", true, "Overlay when you're on fire.")
    @Register val water = Value("UnderWater", true, "Overlay when you're underwater.")
    @Register val wall = Value("InWall", true, "Overlay when you're inside a block.")

    @EventHandler
    fun onRenderOverlay(event: RenderOverlayEvent) {
        when(event.type) {
            RenderOverlayEvent.Type.FIRE -> if(fire.value) event.cancel()
            RenderOverlayEvent.Type.WATER -> if(water.value) event.cancel()
            RenderOverlayEvent.Type.WALL -> if(wall.value) event.cancel()
        }
    }
}