package me.chell.samsara.impl.widget;

import me.chell.samsara.Samsara;
import me.chell.samsara.api.util.Color;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.api.widget.Widget;

public class Watermark extends Widget {

    public Watermark() {
        super("Watermark", 2, 2, 5, 5);
        width = getFontRenderer().getStringWidth(Samsara.NAME + " " + Samsara.VERSION);
        height = getFontRenderer().FONT_HEIGHT;
    }

    private final Value<ColorMode> colorMode = builder("Color", ColorMode.PRIMARY).build();
    private final Value<Color> customColor = builder("Value", Color.WHITE()).visible(b -> colorMode.getValue() == ColorMode.CUSTOM).build();
    private final Value<Boolean> shadow = builder("Shadow", true).build();

    @Override
    public void render() {
        drawString(Samsara.NAME + " " + Samsara.VERSION, getX(), getY(), width, shadow.getValue(), Align.LEFT, colorMode.getValue(), customColor.getValue().getARGB());
    }
}
