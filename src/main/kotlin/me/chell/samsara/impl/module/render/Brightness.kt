package me.chell.samsara.impl.module.render

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.feature.Module
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.event.PlayerTickEvent

class Brightness: Module("Brightness", Category.RENDER) {

    @Register val gamma = Value("Gamma", 10.0, min = 0.0, max = 10.0)

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        mc.options.gamma = gamma.value
    }

    override fun onDisable() {
        super.onDisable()
        mc.options.gamma = 1.0
    }
}