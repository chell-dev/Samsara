package me.chell.samsara

import me.chell.samsara.api.Loadable
import me.chell.samsara.api.event.EventManager
import me.chell.samsara.api.module.ModuleManager
import org.apache.logging.log4j.LogManager

class Samsara {

    companion object {
        const val NAME = "Samsara"
        const val VERSION = "1.0.0"
        const val MODID = "samsara"
        val LOGGER = LogManager.getLogger()
        lateinit var instance: Samsara
    }

    init {
        instance = this
    }

    private val loadables = mutableListOf<Loadable>()

    lateinit var eventManager: EventManager
    lateinit var moduleManager: ModuleManager
    var loaded = false

    fun init() {
        loadables.add(EventManager().also { eventManager = it })
        loadables.add(ModuleManager().also { moduleManager = it })
        load()
        Runtime.getRuntime().addShutdownHook(object: Thread("$NAME shutdown hook") {
            override fun run() {
                if(loaded) unload()
            }
        })
    }

    fun load() {
        for(l in loadables) l.load()
        loaded = true
        LOGGER.info("$NAME $VERSION loaded.")
    }

    fun unload() {
        loaded = false
        for(i in loadables.size-1 downTo 0) loadables[i].unload()
        LOGGER.info("$NAME $VERSION unloaded.")
    }
}