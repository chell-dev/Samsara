package me.chell.samsara.impl.gui;

import me.chell.samsara.api.Loadable;
import me.chell.samsara.api.gui.Drawable;
import me.chell.samsara.api.module.Module;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends GuiScreen implements Loadable {
    private List<Drawable> panels;

    public ClickGUI() {
        panels = new ArrayList<>();
    }

    @Override
    public void load() {
        int x = 20;
        for(Module.Category c : Module.Category.values()) {
            Drawable d = new CategoryPanel(c, x, 20);
            panels.add(d);
            x += d.width + 10;
        }
        panels.add(new ClientPanel(x, 20));
    }

    @Override
    public void unload() {
        panels = new ArrayList<>();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for(Drawable panel : panels) {
            panel.draw(mouseX, mouseY);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for(Drawable panel : panels) {
            if(panel.keyTyped(typedChar, keyCode)) return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for(Drawable panel : panels) {
            if(panel.mouseClicked(mouseX, mouseY, mouseButton)) return;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(Drawable panel : panels) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void onGuiClosed() {
        for(Drawable panel : panels) {
            panel.onGuiClosed();
        }
    }

    @Override
    public void updateScreen() {
        for(Drawable panel : panels) {
            panel.updateScreen();
        }
    }

    @Override
    public void drawDefaultBackground() {}

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
