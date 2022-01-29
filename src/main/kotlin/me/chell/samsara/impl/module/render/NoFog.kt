package me.chell.samsara.impl.module.render

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.feature.Module
import me.chell.samsara.impl.event.RenderFogEvent

class NoFog: Module("NoFog", Category.RENDER) {

    @EventHandler
    fun onRender(event: RenderFogEvent) {
        event.cancel()
    }

}