package me.chell.samsara.impl.gui.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.value.Bind;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.impl.gui.ValueButton;
import org.lwjgl.input.Keyboard;

public class BindButton extends ValueButton<Bind> {
    private boolean listening = false;

    public BindButton(Value<Bind> value, int x, int y) {
        super(x, y, GuiTheme.width, 13, value);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        drawThemedRectSecondary(x+2, y, width-2, height-1);
        drawThemedRectTertiary(x, y, 2, height);
        drawThemedRectTertiary(x+2, y+height-1, width-2, 1);

        drawThemedString(value.getDisplayName(), x + 4, getStringCenterY(y, height-1));

        drawThemedStringRight(listening ? ChatFormatting.GRAY+"..." : Keyboard.getKeyName(value.getValue().getKey()), x + width - 2, getStringCenterY(y, height-1));
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

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        if(listening) {
            switch (keyCode) {
                case Keyboard.KEY_DELETE:
                    value.getValue().setKey(0);
                    break;
                case Keyboard.KEY_ESCAPE:
                    break;
                default:
                    value.getValue().setKey(keyCode);
                    break;
            }
            listening = false;
            return true;
        }
        return super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        listening = false;
    }
}
