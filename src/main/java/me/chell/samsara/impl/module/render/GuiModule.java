package me.chell.samsara.impl.module.render;

import me.chell.samsara.Samsara;
import me.chell.samsara.api.module.Module;
import me.chell.samsara.api.util.Wrapper;
import org.lwjgl.input.Keyboard;

public class GuiModule extends Module {
    public GuiModule() {
        super("ClickGUI", Category.RENDER);
        setBind(Keyboard.KEY_P);
    }

    @Override
    public void onEnable() {
        if(Wrapper.getMinecraft().currentScreen == null)
            Wrapper.getMinecraft().displayGuiScreen(Samsara.INSTANCE.clickGUI);
        toggle();
    }

    @Override
    public void onDisable() {
    }
}
