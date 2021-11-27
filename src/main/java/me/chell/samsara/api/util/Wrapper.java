package me.chell.samsara.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;

import javax.annotation.Nullable;
import java.io.File;

public class Wrapper {

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static FontRenderer getFontRenderer() {
        return getMinecraft().fontRenderer;
    }

    public static PlayerControllerMP getPlayerController() {
        return getMinecraft().playerController;
    }

    public static GameSettings getGameSettings() {
        return getMinecraft().gameSettings;
    }

    @Nullable
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }

    @Nullable
    public static WorldClient getWorld() {
        return getMinecraft().world;
    }

    public static File getGameDir() {
        return getMinecraft().mcDataDir;
    }

    public static ScaledResolution getScaledResolution() {
        return new ScaledResolution(getMinecraft());
    }
}
