package me.chell.samsara.api.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer

interface Wrapper {
    val mc: MinecraftClient
        get() = MinecraftClient.getInstance()

    val fontRenderer: TextRenderer
        get() = mc.textRenderer
}