package me.chell.samsara.impl.gui.buttons;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.impl.gui.ValueButton;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class CharButton extends ValueButton<Character> {
    private boolean listening = false;

    public CharButton(Value<Character> value, int x, int y) {
        super(x, y, GuiTheme.width, 13, value);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        drawThemedRectSecondary(x+2, y, width-2, height-1);
        drawThemedRectTertiary(x, y, 2, height);
        drawThemedRectTertiary(x+2, y+height-1, width-2, 1);

        drawThemedString(value.getDisplayName(), x + 4, getStringCenterY(y, height-1));
        drawThemedStringRight(listening ? ChatFormatting.GRAY+"..." : value.getValue().toString(), x + width - 2, getStringCenterY(y, height-1));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= x+2 && mouseX <= x + width && mouseY >= y && mouseY <= y + height-1) {
            if(mouseButton == 0) {
                listening = !listening;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private final List<Integer> blacklist = Lists.newArrayList(
            Keyboard.KEY_TAB, Keyboard.KEY_CAPITAL, Keyboard.KEY_LSHIFT, Keyboard.KEY_RSHIFT, Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL,
            Keyboard.KEY_LMENU, Keyboard.KEY_RMENU
    );

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        if(listening) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                listening = false;
                return true;
            } else {
                if(!blacklist.contains(keyCode)) {
                    value.setValue(typedChar);
                    listening = false;
                    return true;
                }
            }
        }
        return super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        listening = false;
    }
}
