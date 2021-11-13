package me.chell.samsara;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = Samsara.MOD_ID, name = Samsara.MOD_NAME,version = Samsara.VERSION)
public class Samsara {

    public static final String MOD_ID = "samsara";
    public static final String MOD_NAME = "Samsara";
    public static final String VERSION = "0.1";

    @Mod.Instance(MOD_ID)
    public static Samsara INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
    }

}
