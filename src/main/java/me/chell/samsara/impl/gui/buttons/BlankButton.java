package me.chell.samsara.impl.gui.buttons;

import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.impl.gui.ValueButton;

public class BlankButton extends ValueButton<Object> {

    public BlankButton(Value<Object> value, int x, int y) {
        super(x, y, GuiTheme.width, 13, value);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        drawThemedRectSecondary(x+2, y, width-2, height-1);
        drawThemedRectTertiary(x, y, 2, height);
        drawThemedRectTertiary(x+2, y+height-1, width-2, 1);

        drawThemedString(value.getDisplayName(), x + 4, getStringCenterY(y, height-1));
    }
}
