package me.chell.samsara.impl.module.render

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.module.Module
import me.chell.samsara.impl.event.PlayerTickEvent

class FullBright: Module("FullBright", Category.RENDER) {

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        mc.options.gamma = 10.0
    }

    override fun onDisable() {
        super.onDisable()
        mc.options.gamma = 1.0
    }
}