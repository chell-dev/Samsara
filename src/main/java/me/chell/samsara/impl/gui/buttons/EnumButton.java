package me.chell.samsara.impl.gui.buttons;

import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.impl.gui.ValueButton;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;

public class EnumButton extends ValueButton<Enum<?>> {

    private final List<Enum<?>> enumConstants;

    @SuppressWarnings("unchecked")
    public EnumButton(Value<Enum<?>> value, int x, int y) {
        super(x, y, GuiTheme.width, 13, value);

        enumConstants = Arrays.asList(value.getValue().getClass().getEnumConstants());
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        drawThemedRectSecondary(x+2, y, width-2, height-1);
        drawThemedRectTertiary(x, y, 2, height);
        drawThemedRectTertiary(x+2, y+height-1, width-2, 1);

        drawThemedString(value.getDisplayName(), x + 4, getStringCenterY(y, height-1));
        drawThemedStringRight(Formatting.GRAY+value.getValue().name(), x + width - 2, getStringCenterY(y, height-1));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= x+2 && mouseX <= x + width && mouseY >= y && mouseY <= y + height-1) {
            if(mouseButton == 0) {
                int next = enumConstants.indexOf(value.getValue()) + 1;
                if(next > enumConstants.size() - 1) next = 0;
                value.setValue(enumConstants.get(next));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
