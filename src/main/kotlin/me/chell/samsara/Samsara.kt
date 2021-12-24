package me.chell.samsara

import me.chell.samsara.api.Loadable
import me.chell.samsara.api.event.EventManager
import me.chell.samsara.api.module.ModuleManager
import me.chell.samsara.api.util.Config
import me.chell.samsara.api.util.KillEventManager
import me.chell.samsara.impl.gui.ClickGUI
import net.minecraft.client.option.Option
import net.minecraft.util.Formatting
import org.apache.logging.log4j.LogManager
import java.net.URL

object Samsara {

    const val NAME = "Samsara"
    const val VERSION = "0.0.1"
    const val MODID = "samsara"
    val LOGGER = LogManager.getLogger()
    var MOTD = "No message :("

    private val loadables = mutableListOf<Loadable>()

    var loaded = false

    lateinit var clickGUI: ClickGUI

    fun init() {
        loadables.add(EventManager)
        loadables.add(ModuleManager)
        loadables.add(KillEventManager)
        loadables.add(ClickGUI().also { clickGUI = it })
        load()
        Runtime.getRuntime().addShutdownHook(object: Thread("$NAME shutdown hook") {
            override fun run() {
                if(loaded) unload()
            }
        })
    }

    fun load() {
        for(l in loadables) l.load()
        Config.load("SamsaraConfig.json")
        Option.FOV.setMax(179f)
        loaded = true
        LOGGER.info("$NAME $VERSION loaded.")
    }

    fun unload() {
        loaded = false
        Config.save("SamsaraConfig.json")
        for(i in loadables.size-1 downTo 0) loadables[i].unload()
        Option.FOV.setMax(110f)
        LOGGER.info("$NAME $VERSION unloaded.")
    }

    fun updateMOTD() {
        MOTD = URL("http://ix.io/3IEz").readText()
    }

    fun getMOTDFormatted() = "${Formatting.AQUA}Message Of The Day:${Formatting.RESET} $MOTD".dropLast(1)
}