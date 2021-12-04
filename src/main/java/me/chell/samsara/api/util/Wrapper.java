package me.chell.samsara.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;

public interface Wrapper {

    default Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    default FontRenderer getFontRenderer() {
        return getMinecraft().fontRenderer;
    }

    default PlayerControllerMP getPlayerController() {
        return getMinecraft().playerController;
    }

    default GameSettings getGameSettings() {
        return getMinecraft().gameSettings;
    }

    default EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }

    default WorldClient getWorld() {
        return getMinecraft().world;
    }

    default ScaledResolution getScaledResolution() {
        return new ScaledResolution(getMinecraft());
    }
}
