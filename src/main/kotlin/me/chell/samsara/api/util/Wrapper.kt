package me.chell.samsara.api.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.client.world.ClientWorld

interface Wrapper {
    val mc: MinecraftClient
        get() = MinecraftClient.getInstance()

    val fontRenderer: TextRenderer
        get() = mc.textRenderer

    val player: ClientPlayerEntity
        get() = mc.player!!

    val playerNullable: ClientPlayerEntity?
        get() = mc.player

    val world: ClientWorld
        get() = mc.world!!

    val interactionManager: ClientPlayerInteractionManager
        get() = mc.interactionManager!!

    val networkHandler: ClientPlayNetworkHandler
        get() = player.networkHandler
}