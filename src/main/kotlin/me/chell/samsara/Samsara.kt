package me.chell.samsara

import me.chell.samsara.api.Loadable
import me.chell.samsara.api.event.EventManager
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
    var loaded = false

    fun init() {
        loadables.add(EventManager().also { eventManager = it })
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
        for(l in loadables) l.unload()
        LOGGER.info("$NAME $VERSION unloaded.")
    }
}