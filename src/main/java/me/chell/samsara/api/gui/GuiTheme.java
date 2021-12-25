package me.chell.samsara.api.gui;

import me.chell.samsara.api.util.Color;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.api.value.ValueBuilder;

public class GuiTheme {
    public static int width = 110;

    public static final Value<Integer> borderSize = new ValueBuilder<>("Border Size", 1).bounds(0, 10).build();
    public static final Value<Color> borderColor = new ValueBuilder<>("Border Color", new Color(200, 50, 200, 255)).build();
    public static final Value<Color> primaryColor = new ValueBuilder<>("Primary Color", new Color(200, 50, 200, 255)).build();
    public static final Value<Color> secondaryColor = new ValueBuilder<>("Secondary Color", new Color(50, 50, 50, 200)).build();
    public static final Value<Color> tertiaryColor = new ValueBuilder<>("Tertiary Color", new Color(10, 10, 10, 200)).build();
    public static final Value<Color> textColor = new ValueBuilder<>("Text Color", new Color(255, 255, 255, 255)).build();
}
