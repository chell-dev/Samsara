package me.chell.samsara.impl.gui.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.chell.samsara.api.gui.Drawable;
import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.value.Value;

import java.util.Arrays;
import java.util.List;

public class EnumButton extends Drawable {
    private Value<Enum> value;

    private Class<? extends Enum> enumClass;
    private List<Enum> enumConstants;

    public EnumButton(Value<Enum> value, int x, int y) {
        super(x, y, GuiTheme.width, 12);
        this.value = value;

        enumClass = value.getValue().getClass();
        enumConstants = Arrays.asList(enumClass.getEnumConstants());
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
        drawThemedStringRight(ChatFormatting.GRAY+value.getValue().name(), x + width - 2, y + (height-1)/2 - 4);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
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
