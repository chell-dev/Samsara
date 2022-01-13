package me.chell.samsara.api.util

import club.bottomservices.discordrpc.lib.*
import club.bottomservices.discordrpc.lib.exceptions.NoDiscordException
import me.chell.samsara.api.Loadable
import java.io.IOException

object DiscordUtils: Loadable, Globals {
    private const val samsaraAppId = "918115168599281694"
    //private const val minecraftAppId = ""

    // https://github.com/NepNep21/YARPC-fabric/blob/main/src/main/java/club/bottomservices/discordrpc/fabricmod/YARPC.java
    private val rpcBuilder = RichPresence.Builder().setTimestamps(System.currentTimeMillis() / 1000, null)
    private val rpcClient = DiscordRPCClient(object : EventListener {
        override fun onReady(client: DiscordRPCClient, user: User) {
            LOG.info("Discord RPC ready.")
            client.sendPresence(rpcBuilder.build())
        }

        override fun onError(client: DiscordRPCClient, exception: IOException?, event: ErrorEvent?) {
            LOG.error("Discord RPC Error!")
            LOG.error("Exception: ${exception?: "null"}.")
            LOG.error("ErrorEvent: ${event?.code} | ${event?.message}.")
        }

        override fun onClose(client: DiscordRPCClient) {
            LOG.info("Discord RPC closed.")
            super.onClose(client)
        }
    }, samsaraAppId)

    private val rpcThread = object: Thread("Samsara Discord RPC Thread") {
        override fun run() {
            while(true) {
                if(rpcClient.isConnected)
                    rpcClient.sendPresence(rpcBuilder.build())

                try {
                    sleep(5000)
                } catch (e: InterruptedException) {
                    break
                }
            }
        }
    }.start()

    override fun load() {
        //EventManager.register(this)

        rpcBuilder.largeImage = "cat"
        rpcBuilder.largeText = "hi"
        rpcBuilder.details = "Playing block game."
        rpcBuilder.state = "github.com/chell-dev/Samsara"
    }

    override fun unload() {
        //EventManager.unregister(this)
        stopRPC()
    }

    fun startRPC(): Boolean {
        while (!rpcClient.isConnected) {
            try {
                rpcClient.connect()
            } catch (ignored: NoDiscordException){}
        }
        return rpcClient.isConnected
    }

    fun stopRPC() {
        if(rpcClient.isConnected)
            rpcClient.disconnect()
    }
}