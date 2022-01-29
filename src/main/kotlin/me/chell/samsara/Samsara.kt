package me.chell.samsara

import me.chell.samsara.api.feature.Feature
import me.chell.samsara.api.Loadable
import me.chell.samsara.api.addon.AddonManager
import me.chell.samsara.api.event.EventManager
import me.chell.samsara.api.module.ModuleManager
import me.chell.samsara.api.util.DiscordUtils
import me.chell.samsara.api.util.FileUtils
import me.chell.samsara.api.util.Globals
import me.chell.samsara.api.util.KillEventManager
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.api.widget.WidgetManager
import me.chell.samsara.impl.gui.click.ClickGUI
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.option.Option
import net.minecraft.util.Formatting
import net.minecraft.util.Util
import java.io.File
import java.net.URL
import kotlin.reflect.full.memberProperties

object Samsara: Feature("Client"), Globals {

    private var MOTD = "No message :("

    private val loadables = mutableListOf<Loadable>()

    var loaded = false

    override val values = mutableListOf<Value<*>>()

    @Register(-3) val commandPrefix = Value("Prefix", ',')
    @Register(-2) val lockWidgets = Value("Lock Widgets", false)

    @Register(-1) val openFolder = Value("Open Folder", Runnable {
        Util.getOperatingSystem().open(File("${Globals.NAME}/"))
    })

    @Register(0) val configsValue = Value("Config:", null)

    @Register(1) val modulesFile = Value("Modules", File("Samsara/Modules.json"))
    @Register(2) val widgetsFile = Value("Widgets", File("Samsara/Widgets.json"))
    @Register(3) val waypointFile = Value("Waypoints", File("Samsara/Waypoints.json"))

    @Register(4) val saveConfigs = Value("Save", Runnable { FileUtils.saveConfig() })
    @Register(5) val loadConfigs = Value("Load", Runnable { FileUtils.loadConfig() })

    @Register(6) val themeFile = Value("GUI Theme", File("Samsara/Themes/Default.lua"))
    @Register(7) val loadTheme = Value("Load Theme", Runnable { ClickGUI.INSTANCE.loadTheme(themeFile.value) })
    @Register(8) val downloadThemes = Value("Download Themes", Runnable {
        FileUtils.downloadTheme("Blackout")
    })

    @Register(9) val unload = Value("Unload Client", false)
    @Register(10) val confirm = Value("Confirm Unload", Runnable { mc.setScreen(null); unload() }, visible = { unload.value }, displayName = "Confirm")

    @Register(11) val reload = Value("Reload Client", false)
    @Register(12) val confirm1 = Value("Confirm Reload", Runnable { mc.setScreen(null); unload(); load() }, visible = { reload.value }, displayName = "Confirm")

    fun init() {
        getModInfo()
        FileUtils.createDefaultFiles()
        loadables.add(EventManager)
        loadables.add(DiscordUtils)
        loadables.add(ModuleManager)
        loadables.add(WidgetManager)
        loadables.add(KillEventManager)
        loadables.add(AddonManager)
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
        FileUtils.loadConfig()
        ClickGUI()
        Option.FOV.setMax(179f)
        loaded = true
        LOG.info("${Globals.NAME} ${Globals.VERSION} loaded.")
    }

    fun unload() {
        loaded = false
        FileUtils.saveConfig()
        for(i in loadables.size-1 downTo 0) loadables[i].unload()
        values.clear()
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

        for(v in toAdd.keys) values.add(v)
        values.sortBy { toAdd[it] }
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

    override fun isEnabled() = false
    override fun toggle() {}
    override fun getDisplayName() = name
}