package me.chell.samsara;

import me.chell.samsara.api.Loadable;
import me.chell.samsara.api.module.ModuleManager;
import me.chell.samsara.api.util.Rainbow;
import me.chell.samsara.impl.gui.ClickGUI;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = Samsara.MODID, name = Samsara.NAME,version = Samsara.VERSION)
public class Samsara {
    public static final String MODID = "samsara";
    public static final String NAME = "Samsara";
    public static final String VERSION = "0.1";

    @Mod.Instance(MODID)
    public static Samsara INSTANCE;

    private List<Loadable> loadables;
    public ModuleManager moduleManager;
    public ClickGUI clickGUI;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        loadables = new ArrayList<>();
        loadables.add(moduleManager = new ModuleManager());
        loadables.add(clickGUI = new ClickGUI());
        loadables.add(new Rainbow());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        for(Loadable l : loadables) {
            l.load();
        }
    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for(Loadable l : loadables) {
                l.unload();
            }
        }));
    }

}
