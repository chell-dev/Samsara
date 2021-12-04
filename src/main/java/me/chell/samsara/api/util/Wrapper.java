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

// This should be replaced with a interface -B2H990
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

    public static EntityPlayerSP getPlayer() {
        assert getMinecraft().player != null;
        return getMinecraft().player;
    }

    public static WorldClient getWorld() {
        assert getMinecraft().world != null;
        return getMinecraft().world;
    }

/*
    You don't need this Google context root.
    public static File getGameDir() {
        return getMinecraft().gameDir;
    }*/

    public static ScaledResolution getScaledResolution() {
        return new ScaledResolution(getMinecraft());
    }
}
