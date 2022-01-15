package me.chell.samsara

import me.chell.samsara.api.Loadable
import me.chell.samsara.api.addon.AddonManager
import me.chell.samsara.api.event.EventManager
import me.chell.samsara.api.module.ModuleManager
import me.chell.samsara.api.util.DiscordUtils
import me.chell.samsara.api.util.Globals
import me.chell.samsara.api.util.KillEventManager
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.gui.click.ClickGUI
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.option.Option
import net.minecraft.util.Formatting
import net.minecraft.util.Util
import java.io.File
import java.net.URL
import kotlin.reflect.full.memberProperties

object Samsara: Globals {

    var MOTD = "No message :("

    private val loadables = mutableListOf<Loadable>()

    var loaded = false

    val settings = mutableListOf<Value<*>>()

    @Register(-1) val openFolder = Value("Open Folder", Runnable {
        Util.getOperatingSystem().open(File("${Globals.NAME}/"))
    })

    @Register(0) val configsValue = Value("Config:", null)

    @Register(1) val modulesFile = Value("Modules", File("${Globals.NAME}/Modules.json"))
    @Register(2) val widgetsFile = Value("Widgets", File("${Globals.NAME}/Widgets.json"))
    @Register(3) val waypointFile = Value("Waypoints", File("${Globals.NAME}/Waypoints.json"))

    @Register(4) val saveConfigs = Value("Save", Runnable { })
    @Register(5) val loadConfigs = Value("Load", Runnable { })

    @Register(6) val themeFile = Value("Theme", File("${Globals.NAME}/Themes/DefaultTheme"))
    @Register(7) val loadTheme = Value("Load Theme", Runnable {
        ClickGUI.INSTANCE.loadTheme(themeFile.value.absolutePath)
    })

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
        registerSettings()
        //Config.loadModules("SamsaraConfig.json")
        ClickGUI()
        Option.FOV.setMax(179f)
        loaded = true
        LOG.info("${Globals.NAME} ${Globals.VERSION} loaded.")
    }

    fun unload() {
        loaded = false
        //Config.saveModules("SamsaraConfig.json")
        for(i in loadables.size-1 downTo 0) loadables[i].unload()
        settings.clear()
        Option.FOV.setMax(110f)
        LOG.info("${Globals.NAME} ${Globals.VERSION} unloaded.")
    }

    private fun registerSettings() {
        val toAdd = mutableMapOf<Value<*>, Int>()

        for(p in this::class.memberProperties) {
            for(a in p.annotations) {
                if(a is Register) {
                    val v = p.getter.call(this) as Value<*>
                    toAdd[v] = a.order
                }
            }
        }

        for(v in toAdd.keys) settings.add(v)
        settings.sortBy { toAdd[it] }
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