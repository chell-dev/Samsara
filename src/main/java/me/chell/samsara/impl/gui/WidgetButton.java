package me.chell.samsara.impl.gui;

import me.chell.samsara.api.gui.Drawable;
import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.api.widget.Widget;
import java.util.ArrayList;
import java.util.List;

public class WidgetButton extends Drawable {
    private final Widget widget;
    private final List<ValueButton<?>> valueButtons;
    private final int buttonHeight = 13;
    private boolean open = false;

    public WidgetButton(Widget widget, int x, int y) {
        super(x, y, GuiTheme.width, 13);
        this.widget = widget;

        valueButtons = new ArrayList<>();
        int buttonY = y + buttonHeight;
        for(Value<?> v : widget.getValues()) {
            ValueButton<?> d = ModuleButton.createValueButton(v, x, buttonY);
            buttonY += d.height;
            valueButtons.add(d);
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if(widget.isEnabled()) {
            drawThemedRectPrimary(x, y, width, buttonHeight-1);
        } else {
            drawThemedRectSecondary(x, y, width, buttonHeight-1);
        }
        drawThemedRectTertiary(x, y+buttonHeight-1, width, 1);

        drawThemedString(widget.getName(), x + 2, getStringCenterY(y, buttonHeight-1));

        if(open) {
            int buttonY = y + buttonHeight;
            for(ValueButton<?> button : valueButtons) {
                button.x = x;
                button.y = buttonY;
                if(!button.isVisible()) continue;
                button.draw(mouseX, mouseY);
                buttonY += button.height;
            }
            height = buttonY - y;
        } else {
            height = buttonHeight;
        }

        if(widget.isEnabled())
            widget.draw(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + buttonHeight-1) {
            if (mouseButton == 0) {
                widget.toggle();
                return true;
            } else if (mouseButton == 1) {
                open = !open;
                return true;
            }
        }

        for(ValueButton<?> button : valueButtons) {
            if(!button.isVisible()) continue;
            if(button.mouseClicked(mouseX, mouseY, mouseButton)) return true;
        }

        if(widget.isEnabled())
            return widget.mouseClicked(mouseX, mouseY, mouseButton);
        else
            return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for(ValueButton<?> button : valueButtons) {
            button.mouseReleased(mouseX, mouseY, state);
        }

        widget.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        for(ValueButton<?> button : valueButtons) {
            if(!button.isVisible()) continue;
            if(button.keyTyped(typedChar, keyCode)) return true;
        }

        if(widget.isEnabled())
            return widget.keyTyped(typedChar, keyCode);
        else
            return super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        for(ValueButton<?> button : valueButtons) {
            if(!button.isVisible()) continue;
            button.updateScreen();
        }

        widget.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        for(ValueButton<?> button : valueButtons) {
            button.onGuiClosed();
        }

        widget.onGuiClosed();
    }
}
