package me.chell.samsara.api.util;

import me.chell.samsara.api.Loadable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Rainbow implements Loadable {

    private static float hue = 0f;

    public static Color getColor(int alpha) {
        int rgb = java.awt.Color.HSBtoRGB(hue, 1f, 1f);
        return new Color(rgb, alpha);
    }

    public static int getARGB(int alpha) {
        return getColor(alpha).getARGB();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        hue += ClientValues.rainbowSpeed.getValue() / 2000f;
        if(hue >= 1f) hue = 0f;
    }

    @Override
    public void load() {
        MinecraftForge.EVENT_BUS.register(this);
        hue = 0f;
    }

    @Override
    public void unload() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
