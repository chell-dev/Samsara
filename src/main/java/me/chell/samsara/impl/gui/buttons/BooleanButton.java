package me.chell.samsara.impl.gui.buttons;

import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.impl.gui.ValueButton;

public class BooleanButton extends ValueButton<Boolean> {

    public BooleanButton(Value<Boolean> value, int x, int y) {
        super(x, y, GuiTheme.width, 13, value);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if(value.getValue()) {
            drawThemedRectPrimary(x+2, y, width-2, height-1);
        } else {
            drawThemedRectSecondary(x+2, y, width-2, height-1);
        }
        drawThemedRectTertiary(x, y, 2, height);
        drawThemedRectTertiary(x+2, y+height-1, width-2, 1);

        drawThemedString(value.getDisplayName(), x + 4, getStringCenterY(y, height-1));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= x+2 && mouseX <= x + width && mouseY >= y && mouseY <= y + height-1) {
            if(mouseButton == 0) {
                value.setValue(!value.getValue());
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
