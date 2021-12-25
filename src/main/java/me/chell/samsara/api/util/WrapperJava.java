package me.chell.samsara.api.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

public interface WrapperJava {
    default MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }

    default TextRenderer getFontRenderer() {
        return getMc().textRenderer;
    }
}
