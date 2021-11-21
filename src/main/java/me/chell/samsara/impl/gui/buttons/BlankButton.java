package me.chell.samsara.impl.gui.buttons;

import me.chell.samsara.api.gui.Drawable;
import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.value.Value;

public class BlankButton extends Drawable {
    private Value<?> value;

    public BlankButton(Value<?> value, int x, int y) {
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

        drawThemedRectSecondary(x+2, y, width-2, height-1);
        drawThemedRectTertiary(x, y, 2, height);
        drawThemedRectTertiary(x+2, y+height-1, width-2, 1);

        drawThemedString(value.getDisplayName(), x + 4, y + (height-1)/2 - 4);
    }
}
