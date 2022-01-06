package me.chell.samsara.impl.gui.buttons;

import com.google.common.collect.Lists;
import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.impl.gui.ValueButton;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.List;

public class Slider<T extends Number> extends ValueButton<T> {
    private int sliderWidth = 0;
    private boolean grabbed = false;

    private int ticks;
    private boolean editing = false;
    private String oldText, editText;

    public void setSliderWidth() {
        double min = value.getSliderMin().doubleValue();
        double max = value.getSliderMax().doubleValue();
        double val = value.getValue().doubleValue() - min;
        double diff = min >= 0 ? max - min : min - max;
        if(val == 0) sliderWidth = 0;
        double d = val / diff;
        if(d < 0) d *= -1;
        sliderWidth = (int) ((width-2) * MathHelper.clamp(d, 0D, 1D));
    }

    public Slider(Value<T> value, int x, int y) {
        super(x, y, GuiTheme.width, 13, value);
        editText = ""+value.getValue();

        setSliderWidth();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void draw(int mouseX, int mouseY) {
        if(value.getName().equals("Rotation")) {
            double min = value.getSliderMin().doubleValue();
            double max = value.getSliderMax().doubleValue();
            double val = value.getValue().doubleValue() - min;
            double diff = min >= 0 ? max - min : min - max;
            if(val == 0) sliderWidth = 0;
            double d = val / diff;
            if(d < 0) d *= -1;
            sliderWidth = (int) ((width-2) * MathHelper.clamp(d, 0D, 1D));
        }

        if(grabbed) {
            int mousePos = MathHelper.clamp(mouseX - (x+2), 0, width-2);

            double min = value.getSliderMin().doubleValue();
            double max = value.getSliderMax().doubleValue();
            double diff = max - min;

            double v = mousePos / (width-2D);
            v *= diff;
            v += min;

            if(value.getSliderMin() instanceof Integer) {
                value.setValue((T) (Number) (int) v);
            } else
                value.setValue((T) (Number) (Math.round(v * 100d) / 100d));

            setSliderWidth();
        }

        drawThemedRectPrimary(x+2, y, sliderWidth, height-1);
        drawThemedRectSecondary(x+2+sliderWidth, y, width-2-sliderWidth, height-1);
        drawThemedRectTertiary(x, y, 2, height);
        drawThemedRectTertiary(x+2, y+height-1, width-2, 1);

        String displayText;
        if(editing) {
            displayText = editText;
            if(ticks / 6 % 2 == 0) displayText += "_";
        } else {
            displayText = ""+value.getValue();
        }

        drawThemedString(value.getDisplayName(), x + 4, getStringCenterY(y, height-1));
        int rightX = x + width - 2;
        if(displayText.endsWith("_")) rightX += getFontRenderer().getWidth("_");
        drawThemedStringRight(displayText, rightX, getStringCenterY(y, height-1));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= x+2 && mouseX <= x + width && mouseY >= y && mouseY <= y + height-1) {
            if(mouseButton == 0) {
                grabbed = true;
                return true;
            } else if(mouseButton == 2) {
                if(!editing) {
                    ticks = 0;
                    editing = true;
                    editText = oldText = ""+value.getValue();
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if(state == 0) {
            grabbed = false;
        }
    }

    private final List<Character> numbers = Lists.newArrayList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-');

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        if(editing) {
            switch (keyCode) {
                case GLFW.GLFW_KEY_BACKSPACE:
                    editText = StringUtils.chop(editText);
                    return true;
                case GLFW.GLFW_KEY_ENTER:
                    parseValue(editText);
                    editing = false;
                    return true;
                case GLFW.GLFW_KEY_ESCAPE:
                    editing = false;
                    editText = oldText;
                    return true;
                case GLFW.GLFW_KEY_V:
                    if (InputUtil.isKeyPressed(getMc().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
                        editText += Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
                    } else {
                        editText += typedChar;
                    }
                    return true;
                default:
                    if(numbers.contains(typedChar)) {
                        editText += typedChar;
                        return true;
                    }
            }
        }
        return super.keyTyped(typedChar, keyCode);
    }

    @SuppressWarnings("unchecked")
    private void parseValue(String text) {
        if (value.getSliderMin() instanceof Integer) {
            try {
                value.setValue((T) (Integer) Integer.parseInt(text));
                setSliderWidth();
            } catch (NumberFormatException ignored) {}
        }
        else if (value.getSliderMin() instanceof Float) {
            try {
                value.setValue((T) (Float) Float.parseFloat(text));
                setSliderWidth();
            } catch (NumberFormatException ignored) {}
        }
        else if(value.getSliderMin() instanceof Double) {
            try {
                value.setValue((T) (Double) Double.parseDouble(text));
                setSliderWidth();
            } catch (NumberFormatException ignored) {}
        }
    }

    @Override
    public void onGuiClosed() {
        grabbed = false;
        editing = false;
        editText = oldText;
    }

    @Override
    public void updateScreen() {
        ticks++;
    }
}
