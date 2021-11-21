package me.chell.samsara.impl.gui.buttons;

import me.chell.samsara.api.gui.Drawable;
import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.value.Value;

public class BooleanButton extends Drawable {
    private Value<Boolean> value;

    public BooleanButton(Value<Boolean> value, int x, int y) {
        super(x, y, GuiTheme.width, 12);
        this.value = value;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if(!value.isVisible()) {
            height = 0;
            return;
        } else {
            height = 12;
        }
        if(value.getValue()) {
            drawThemedRectPrimary(x+2, y, width-2, height-1);
        } else {
            drawThemedRectSecondary(x+2, y, width-2, height-1);
        }
        drawThemedRectTertiary(x, y, 2, height);
        drawThemedRectTertiary(x+2, y+height-1, width-2, 1);

        drawThemedString(value.getDisplayName(), x + 4, y + (height-1)/2 - 4);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
            if(mouseButton == 0) {
                value.setValue(!value.getValue());
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
