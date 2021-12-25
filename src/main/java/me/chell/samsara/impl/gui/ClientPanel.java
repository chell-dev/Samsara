package me.chell.samsara.impl.gui;

import me.chell.samsara.api.gui.Drawable;
import me.chell.samsara.api.gui.GuiTheme;
import java.util.ArrayList;
import java.util.List;

public class ClientPanel extends Drawable {
    private final int titleHeight = 17;

    private boolean open = true;
    private final List<ValueButton<?>> buttons;

    private boolean grabbed = false;
    private int offsetX, offsetY;

    private boolean editing = false;
    private String oldText, editText, title;
    private int ticks = 0;

    public ClientPanel(int x, int y) {
        super(x, y, GuiTheme.width, 17);
        editText = title = "Client";

        buttons = new ArrayList<>();
        int buttonY = y + titleHeight;
        /*
        for(Value<?> v : ClientValues.values) {
            ValueButton<?> d = ModuleButton.createValueButton(v, x, buttonY);
            buttons.add(d);
            buttonY += d.height;

        }

        Value<Object> gui = new ValueBuilder<>("GUI Settings:", null).build();
        ValueButton<?> guiButton = ModuleButton.createValueButton(gui, x, buttonY);
        buttons.add(guiButton);
        buttonY += guiButton.height;

        for(Value<?> v : GuiTheme.values) {
            ValueButton<?> d = ModuleButton.createValueButton(v, x, buttonY);
            buttons.add(d);
            buttonY += d.height;
        }
         */
        height = buttonY - y;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if(grabbed) {
            x = mouseX - offsetX;
            y = mouseY - offsetY;
        }

        drawThemedBorder(x, y, width, height);
        drawThemedRectPrimary(x, y, width, titleHeight-1);
        drawThemedRectTertiary(x, y+titleHeight-1, width, 1);

        String displayText;
        if(editing) {
            displayText = editText;
            if(ticks / 6 % 2 == 0) displayText += "_";
        } else {
            displayText = title;
        }

        drawThemedString(displayText, x + 2, getStringCenterY(y, titleHeight-1));

        if(open) {
            int buttonY = y + titleHeight;
            for(ValueButton<?> button : buttons) {
                button.x = x;
                button.y = buttonY;
                if(!button.isVisible()) continue;
                button.draw(mouseX, mouseY);
                buttonY += button.height;
            }
            height = buttonY - y;
        } else {
            height = titleHeight;
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + titleHeight-1) {
            switch (mouseButton) {
                case 0:
                    offsetX = mouseX - x;
                    offsetY = mouseY - y;
                    grabbed = true;
                    return true;
                case 1:
                    open = !open;
                    return true;
                case 2:
                    if(!editing) {
                        ticks = 0;
                        editing = true;
                        oldText = title;
                    }
                    return true;
            }
        }

        if(!open) return super.mouseClicked(mouseX, mouseY, mouseButton);

        for(ValueButton<?> button : buttons) {
            if(!button.isVisible()) continue;
            if(button.mouseClicked(mouseX, mouseY, mouseButton)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if(state == 0) grabbed = false;
        for(ValueButton<?> button : buttons) {
            button.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        if(editing) {
            editing = false;
        }

        for(ValueButton<?> button : buttons) {
            if(!button.isVisible()) continue;
            if(button.keyTyped(typedChar, keyCode)) return true;
        }
        return super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        ticks++;
        for(ValueButton<?> button : buttons) {
            if(!button.isVisible()) continue;
            button.updateScreen();
        }
    }

    @Override
    public void onGuiClosed() {
        for(ValueButton<?> button : buttons) {
            button.onGuiClosed();
        }
    }
}
