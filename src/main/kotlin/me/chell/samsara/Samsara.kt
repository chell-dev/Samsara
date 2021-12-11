package me.chell.samsara

import org.apache.logging.log4j.LogManager

class Samsara {

    companion object {
        const val NAME = "Samsara"
        const val VERSION = "1.0.0"
        const val MODID = "samsara"
        val LOGGER = LogManager.getLogger()
    }

    fun init() {
        load()
    }

    fun load() {
        LOGGER.info("$NAME $VERSION loaded.")
    }

    fun unload() {
    }
}