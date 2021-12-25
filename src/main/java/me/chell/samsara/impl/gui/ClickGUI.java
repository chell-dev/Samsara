package me.chell.samsara.impl.gui;

import me.chell.samsara.api.Loadable;
import me.chell.samsara.api.gui.Drawable;
import me.chell.samsara.api.module.Module;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen implements Loadable {
    private List<Drawable> panels;

    public ClickGUI() {
        super(new LiteralText("ClickGUI"));
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
        /*
        ClientPanel clientPanel = new ClientPanel(x, 20);
        panels.add(clientPanel);
        x += clientPanel.width + 10;

        panels.add(new WidgetsPanel(x, 20));
        */
    }

    @Override
    public void unload() {
        panels = new ArrayList<>();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for(Drawable panel : panels) {
            panel.draw(mouseX, mouseY);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for(Drawable panel : panels) {
            if(panel.keyTyped('_', keyCode)) return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        for(Drawable panel : panels) {
            panel.onGuiClosed();
        }
        super.onClose();
    }

    @Override
    public void tick() {
        for(Drawable panel : panels) {
            panel.updateScreen();
        }
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(Drawable panel : panels) {
            if(panel.mouseClicked((int)mouseX, (int)mouseY, button)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for(Drawable panel : panels) {
            panel.mouseReleased((int)mouseX, (int)mouseY, 0);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for(Drawable panel : panels) {
            if(panel.keyTyped(chr, 0)) return true;
        }
        return super.charTyped(chr, modifiers);
    }

}
