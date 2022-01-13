package me.chell.samsara.impl.module.misc

import me.chell.samsara.api.module.Module
import me.chell.samsara.api.util.DiscordUtils

// TODO customization
class DiscordRPC: Module("DiscordRPC", Category.MISC) {

    override fun toggle() {
        if(isEnabled()) {
            bind.value.enabled = false
            DiscordUtils.stopRPC()
        } else {
            if(DiscordUtils.startRPC())
                bind.value.enabled = true
        }
    }

    override fun onEnable() {}

    override fun onDisable() {}
}