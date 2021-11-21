package me.chell.samsara.impl.gui;

import com.google.common.collect.Lists;
import me.chell.samsara.api.gui.Drawable;
import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.module.Module;
import me.chell.samsara.api.util.Color;
import me.chell.samsara.api.value.Bind;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.impl.gui.buttons.*;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton extends Drawable {
    private boolean open = false;
    private Module module;
    private CategoryPanel parent;
    private int buttonHeight;
    private List<Drawable> valueButtons;

    private boolean editing = false;
    private String oldText;
    private String editText;
    private int ticks = 0;

    public ModuleButton(Module module, int x, int y, CategoryPanel parent) {
        super(x, y, GuiTheme.width, 13);
        this.module = module;
        this.parent = parent;
        buttonHeight = 13;

        editText = module.getDisplayName();

        valueButtons = new ArrayList<>();
        int buttonY = y + buttonHeight;
        for(Value<?> v : module.getValues()) {
            Drawable d = createValueButton(v, x, buttonY);
            buttonY += d.height;
            valueButtons.add(d);
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if(module.isEnabled()) {
            drawThemedRectPrimary(x, y, width, buttonHeight-1);
        } else {
            drawThemedRectSecondary(x, y, width, buttonHeight-1);
        }
        drawThemedRectTertiary(x, y+buttonHeight-1, width, 1);

        String displayText;
        if(editing) {
            displayText = editText;
            if(ticks / 6 % 2 == 0) displayText += "_";
        } else {
            displayText = module.getDisplayName();
        }

        drawThemedString(displayText, x + 2, y + (buttonHeight-1)/2 - 4);

        if(open) {
            int buttonY = y + buttonHeight;
            for(Drawable button : valueButtons) {
                button.x = x;
                button.y = buttonY;
                button.draw(mouseX, mouseY);
                buttonY += button.height;
            }
            height = buttonY - y;
        } else {
            height = buttonHeight;
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + buttonHeight) {
            switch (mouseButton) {
                case 0:
                    module.toggle();
                    return true;
                case 1:
                    open = !open;
                    return true;
                case 2:
                    if(!editing) {
                        ticks = 0;
                        editing = true;
                        oldText = module.getDisplayName();
                    }
                    return true;
            }
        }

        for(Drawable button : valueButtons) {
            if(button.mouseClicked(mouseX, mouseY, mouseButton)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for(Drawable button : valueButtons) {
            button.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        if(editing) {
            switch (keyCode) {
                case Keyboard.KEY_BACK:
                    editText = StringUtils.chop(editText);
                    return true;
                case Keyboard.KEY_RETURN:
                    module.setDisplayName(editText);
                    editing = false;
                    return true;
                case Keyboard.KEY_ESCAPE:
                    editing = false;
                    editText = oldText;
                    return true;
                case Keyboard.KEY_V:
                    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                        editText += Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
                    } else {
                        editText += typedChar;
                    }
                    return true;
                default:
                    if(!blacklist.contains(keyCode)) {
                        editText += typedChar;
                        return true;
                    }
            }
        }

        for(Drawable button : valueButtons) {
            if(button.keyTyped(typedChar, keyCode)) return true;
        }
        return super.keyTyped(typedChar, keyCode);
    }

    private final List<Integer> blacklist = Lists.newArrayList(
            Keyboard.KEY_TAB, Keyboard.KEY_CAPITAL, Keyboard.KEY_LSHIFT, Keyboard.KEY_RSHIFT, Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL,
            Keyboard.KEY_LMENU, Keyboard.KEY_RMENU
    );

    @Override
    public void updateScreen() {
        ticks++;
        for(Drawable button : valueButtons) {
            button.updateScreen();
        }
    }

    @Override
    public void onGuiClosed() {
        for(Drawable button : valueButtons) {
            button.onGuiClosed();
        }
    }

    @SuppressWarnings("unchecked")
    public static Drawable createValueButton(Value<?> value, int x, int y) { // TODO: string
        if(value.getValue() instanceof Boolean) return new BooleanButton((Value<Boolean>)value, x, y);
        if(value.getValue() instanceof Number) return new Slider<>((Value<Number>) value, x, y);
        if(value.getValue() instanceof Bind) return new BindButton((Value<Bind>) value, x, y);
        if(value.getValue() instanceof Color) return new ColorPicker((Value<Color>) value, x, y);
        if(value.getValue() instanceof Enum) return new EnumButton((Value<Enum>) value, x, y);
        if(value.getValue() instanceof Character) return new CharButton((Value<Character>) value, x, y);
        return new BlankButton(value, x, y);
    }
}
