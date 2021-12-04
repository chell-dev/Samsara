package me.chell.samsara.impl.gui;

import com.google.common.collect.Lists;
import me.chell.samsara.Samsara;
import me.chell.samsara.api.gui.Drawable;
import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.module.Module;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryPanel extends Drawable {
    private final int titleHeight = 17;
    private final Module.Category category;
    private final List<ModuleButton> buttons;
    private boolean open = true;

    private boolean grabbed = false;
    private int offsetX, offsetY;

    private boolean editing = false;
    private String oldText, editText;
    public int ticks = 0;

    public CategoryPanel(Module.Category category, int x, int y) {
        super(x, y, GuiTheme.width, 17);
        this.category = category;
        editText = category.getName();

        buttons = new ArrayList<>();
        int buttonY = y + titleHeight;
        for(Module m : Samsara.INSTANCE.moduleManager.getModules()) {
            if(m.getCategory().equals(category)) {
                ModuleButton b = new ModuleButton(m, x, buttonY, this);
                buttons.add(b);
                buttonY += b.height;
            }
        }
        height = buttonY - y;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if(grabbed) {
            x = mouseX - offsetX;
            y = mouseY - offsetY;
        }

        drawThemedBorder(x, y, width, height);
        drawThemedRectPrimary(x, y, width, titleHeight-1);
        drawThemedRectTertiary(x, y+titleHeight-1, width, 1);

        String displayText;
        if(editing) {
            displayText = editText;
            if(ticks / 6 % 2 == 0) displayText += "_";
        } else {
            displayText = category.getName();
        }

        drawThemedString(displayText, x + 2, getStringCenterY(y, titleHeight-1));

        if(open) {
            int buttonY = y + titleHeight;
            for(ModuleButton button : buttons) {
                button.x = x;
                button.y = buttonY;
                button.draw(mouseX, mouseY);
                buttonY += button.height;
            }
            height = buttonY - y;
        } else {
            height = titleHeight;
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + titleHeight-1) {
            switch (mouseButton) {
                case 0:
                    offsetX = mouseX - x;
                    offsetY = mouseY - y;
                    grabbed = true;
                    return true;
                case 1:
                    open = !open;
                    return true;
                case 2:
                    if(!editing) {
                        ticks = 0;
                        editing = true;
                        oldText = category.getName();
                    }
                    return true;
            }
        }

        if(!open) return super.mouseClicked(mouseX, mouseY, mouseButton);

        for(ModuleButton button : buttons) {
            if(button.mouseClicked(mouseX, mouseY, mouseButton)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if(state == 0) grabbed = false;
        for(ModuleButton button : buttons) {
            button.mouseReleased(mouseX, mouseY, state);
        }
    }

    public final List<Integer> blacklist = Lists.newArrayList(
            Keyboard.KEY_TAB, Keyboard.KEY_CAPITAL, Keyboard.KEY_LSHIFT, Keyboard.KEY_RSHIFT, Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL,
            Keyboard.KEY_LMENU, Keyboard.KEY_RMENU
    );

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        if(editing) {
            switch (keyCode) {
                case Keyboard.KEY_BACK:
                    editText = StringUtils.chop(editText);
                    return true;
                case Keyboard.KEY_RETURN:
                    category.setName(editText);
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

        for(ModuleButton button : buttons) {
            if(button.keyTyped(typedChar, keyCode)) return true;
        }
        return super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        ticks++;
        for(ModuleButton button : buttons) {
            button.updateScreen();
        }
    }

    @Override
    public void onGuiClosed() {
        for(ModuleButton button : buttons) {
            button.onGuiClosed();
        }
    }
}
