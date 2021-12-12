package me.chell.samsara.api.util

import net.minecraft.client.MinecraftClient

interface Wrapper {
    val minecraft: MinecraftClient
        get() = MinecraftClient.getInstance()
}