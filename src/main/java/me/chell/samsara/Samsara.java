package me.chell.samsara;

import me.chell.samsara.api.Loadable;
import me.chell.samsara.api.module.ModuleManager;
import me.chell.samsara.api.util.*;
import me.chell.samsara.api.widget.WidgetManager;
import me.chell.samsara.impl.gui.ClickGUI;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = Samsara.MODID, name = Samsara.NAME,version = Samsara.VERSION)
public class Samsara {
    public static final String MODID = "samsara";
    public static final String NAME = "Samsara";
    public static final String VERSION = "1.0.0";

    public static final File FOLDER = new File(NAME + File.separatorChar);
    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.Instance(MODID)
    public static Samsara INSTANCE;

    private List<Loadable> loadables;
    public ModuleManager moduleManager;
    public WidgetManager widgetManager;
    public ClickGUI clickGUI;
    public Config config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        loadables = new ArrayList<>();
        new ChatUtil();
        new ClientValues();
        loadables.add(moduleManager = new ModuleManager());
        loadables.add(widgetManager = new WidgetManager());
        loadables.add(clickGUI = new ClickGUI());
        loadables.add(new Rainbow());
        loadables.add(new KillEventManager());
        config = new Config();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        for(Loadable l : loadables) {
            l.load();
        }
        config.load();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            config.save();
            for(Loadable l : loadables) {
                l.unload();
            }
        }));
    }

}