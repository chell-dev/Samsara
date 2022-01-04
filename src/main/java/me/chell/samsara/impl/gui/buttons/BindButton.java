package me.chell.samsara.impl.gui.buttons;

import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.value.Bind;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.impl.gui.ValueButton;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

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

        String key = InputUtil.fromKeyCode(value.getValue().getKey(), 0).getLocalizedText().asString().toUpperCase();
        String text = (value.getValue().getHold() ? "HOLD " : "") + (key.isBlank() ? "UNKNOWN" : key);
        drawThemedStringRight(Formatting.GRAY + (listening ? "..." : text), x + width - 2, getStringCenterY(y, height-1));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= x+2 && mouseX <= x + width && mouseY >= y && mouseY <= y + height-1) {
            if(listening) {
                value.getValue().setKey(mouseButton);
                listening = false;
            }
            else if(mouseButton == 0) {
                listening = true;
                return true;
            }
            else if(mouseButton == 1) {
                value.getValue().setHold(!value.getValue().getHold());
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        if(listening) {
            switch (keyCode) {
                case GLFW.GLFW_KEY_DELETE:
                    value.getValue().setKey(-1);
                    break;
                case GLFW.GLFW_KEY_ESCAPE:
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
