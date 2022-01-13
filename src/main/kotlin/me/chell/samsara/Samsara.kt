package me.chell.samsara

import me.chell.samsara.api.Loadable
import me.chell.samsara.api.addon.AddonManager
import me.chell.samsara.api.event.EventManager
import me.chell.samsara.api.module.ModuleManager
import me.chell.samsara.api.util.DiscordUtils
import me.chell.samsara.api.util.Globals
import me.chell.samsara.api.util.KillEventManager
import me.chell.samsara.impl.gui.click.ClickGUI
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.option.Option
import net.minecraft.util.Formatting
import java.net.URL

object Samsara: Globals {

    var MOTD = "No message :("

    private val loadables = mutableListOf<Loadable>()

    var loaded = false

    fun init() {
        getModInfo()
        loadables.add(EventManager)
        loadables.add(ModuleManager)
        loadables.add(KillEventManager)
        loadables.add(AddonManager)
        loadables.add(DiscordUtils)
        load()
        Runtime.getRuntime().addShutdownHook(object: Thread("${Globals.NAME} shutdown hook") {
            override fun run() {
                if(loaded) unload()
            }
        })
    }

    fun load() {
        for(l in loadables) l.load()
        ClickGUI.INSTANCE = ClickGUI()
        //Config.loadModules("SamsaraConfig.json")
        Option.FOV.setMax(179f)
        loaded = true
        LOG.info("${Globals.NAME} ${Globals.VERSION} loaded.")
    }

    fun unload() {
        loaded = false
        //Config.saveModules("SamsaraConfig.json")
        for(i in loadables.size-1 downTo 0) loadables[i].unload()
        Option.FOV.setMax(110f)
        LOG.info("${Globals.NAME} ${Globals.VERSION} unloaded.")
    }

    private fun getModInfo() {
        val mod = FabricLoader.getInstance().getModContainer(MODID).get()
        Globals.NAME = mod.metadata.name
        Globals.VERSION = mod.metadata.version.friendlyString
    }

    fun updateMOTD() {
        MOTD = URL("http://ix.io/3IEz").readText()
    }

    fun getMOTDFormatted() = "${Formatting.AQUA}Message Of The Day:${Formatting.RESET} $MOTD".dropLast(1)
}