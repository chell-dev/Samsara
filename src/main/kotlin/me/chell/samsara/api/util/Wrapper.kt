package me.chell.samsara.api.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.network.ClientPlayerEntity

interface Wrapper {
    val mc: MinecraftClient
        get() = MinecraftClient.getInstance()

    val fontRenderer: TextRenderer
        get() = mc.textRenderer

    val player: ClientPlayerEntity
        get() = mc.player!!

    val playerNullable: ClientPlayerEntity?
        get() = mc.player
}