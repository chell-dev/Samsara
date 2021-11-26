package me.chell.samsara.impl.gui.buttons;

import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.impl.gui.ValueButton;
import net.minecraft.util.math.MathHelper;

public class Slider<T> extends ValueButton<T> {
    private int sliderWidth = 0;
    private boolean grabbed = false;
    private Type type;

    //@SuppressWarnings("unchecked")
    public Slider(Value<T> value, int x, int y) {
        super(x, y, GuiTheme.width, 13, value);

        // TODO fix
        if(value.getValue() instanceof Integer){
            type = Type.INT;

            /*
            Value<Integer> val = (Value<Integer>) value;
            int range = val.getSliderMax() - val.getSliderMin();
            int m = (val.getValue() * (2-width)) / range;
            sliderWidth = m * -1;
            */
        }
        else if(value.getValue() instanceof Double) {
            type = Type.DOUBLE;

            /*
            Value<Double> val = (Value<Double>) value;
            double range = val.getSliderMax() - val.getSliderMin();
            double m = (val.getValue() * (2-width)) / range;
            sliderWidth = (int)m * -1;
            */
        }
        else if(value.getValue() instanceof Float) {
            type = Type.FLOAT;

            /*
            Value<Float> val = (Value<Float>) value;
            float range = val.getSliderMax() - val.getSliderMin();
            float m = (val.getValue() * (2-width)) / range;
            sliderWidth = (int)m * -1;
            */
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void draw(int mouseX, int mouseY) {
        if(grabbed) {
            int mousePos = MathHelper.clamp(mouseX - (x+2), 0, width-2);
            sliderWidth = mousePos;

            switch (type) {
                case INT:
                    Value<Integer> val = (Value<Integer>) value;
                    int range = val.getSliderMax() - val.getSliderMin();
                    float v = val.getSliderMin() + (mousePos / (width-2f) * range);
                    value.setValue((T)(Integer)(int)v); // don't worry about it
                    break;
                case DOUBLE:
                    Value<Double> val2 = (Value<Double>) value;
                    double range2 = val2.getSliderMax() - val2.getSliderMin();
                    double v2 = val2.getSliderMin() + ((double)mousePos / (width-2) * range2);
                    double roundedD = Math.round(v2 * 100d) / 100d;
                    value.setValue((T)(Double)roundedD);
                    break;
                case FLOAT:
                    Value<Float> val3 = (Value<Float>) value;
                    float range3 = val3.getSliderMax() - val3.getSliderMin();
                    float v3 = val3.getSliderMin() + ((float)mousePos / (width-2) * range3);
                    float roundedF = Math.round(v3 * 100f) / 100f;
                    value.setValue((T)(Float)roundedF);
                    break;
            }
        }

        drawThemedRectPrimary(x+2, y, sliderWidth, height-1);
        drawThemedRectSecondary(x+2+sliderWidth, y, width-2-sliderWidth, height-1);
        drawThemedRectTertiary(x, y, 2, height);
        drawThemedRectTertiary(x+2, y+height-1, width-2, 1);

        drawThemedString(value.getDisplayName(), x + 4, getStringCenterY(y, height-1));
        drawThemedStringRight(""+value.getValue(), x + width - 2, getStringCenterY(y, height-1));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= x+2 && mouseX <= x + width && mouseY >= y && mouseY <= y + height-1) {
            if(mouseButton == 0) {
                grabbed = true;
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

    @Override
    public void onGuiClosed() {
        grabbed = false;
    }

    private enum Type {
        INT, DOUBLE, FLOAT
    }
}
