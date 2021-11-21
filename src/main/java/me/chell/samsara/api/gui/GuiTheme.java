package me.chell.samsara.api.gui;

import me.chell.samsara.api.util.Color;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.api.value.ValueBuilder;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class GuiTheme {
    public static List<Value<?>> values = new ArrayList<>();

    public static int width = 110;

    public static ResourceLocation primaryTex = new ResourceLocation("samsara", "textures/gui/primary.png");
    public static ResourceLocation secondaryTex = new ResourceLocation("samsara", "textures/gui/secondary.png");
    public static ResourceLocation tertiaryTex = new ResourceLocation("samsara", "textures/gui/tertiary.png");
    public static ResourceLocation borderTex = new ResourceLocation("samsara", "textures/gui/border.png");

    public static Value<Integer> borderSize = builder("Border Size", 1).bounds(0, 10).build();
    public static Value<Mode> borderMode = builder("Border Mode", Mode.COLOR).name("Border").build();
    public static Value<Color> borderColor = builder("Border Color", new Color(200, 50, 200, 255)).name("Color").visible(b -> borderMode.getValue().equals(Mode.COLOR)).build();
    public static Value<Color> borderStart = builder("Border Start", Color.RED).name("Start").visible(b -> borderMode.getValue().equals(Mode.H_GRADIENT) || borderMode.getValue().equals(Mode.V_GRADIENT)).build();
    public static Value<Color> borderEnd = builder("Border End", Color.BLUE).name("End").visible(b -> borderMode.getValue().equals(Mode.H_GRADIENT) || borderMode.getValue().equals(Mode.V_GRADIENT)).build();
    public static Value<Integer> borderAlpha = builder("Border Alpha", 255).name("Alpha").bounds(0, 255).visible(b -> borderMode.getValue().equals(Mode.RAINBOW)).build();

    public static Value<Mode> primaryMode = builder("Primary Mode", Mode.H_GRADIENT).name("Primary").build();
    public static Value<Color> primaryColor = builder("Primary Color", new Color(200, 50, 200, 255)).name("Color").visible(b -> primaryMode.getValue().equals(Mode.COLOR)).build();
    public static Value<Color> primaryStart = builder("Primary Start", Color.RED).name("Start").visible(b -> primaryMode.getValue().equals(Mode.H_GRADIENT) || borderMode.getValue().equals(Mode.V_GRADIENT)).build();
    public static Value<Color> primaryEnd = builder("Primary End", Color.BLUE).name("End").visible(b -> primaryMode.getValue().equals(Mode.H_GRADIENT) || borderMode.getValue().equals(Mode.V_GRADIENT)).build();
    public static Value<Integer> primaryAlpha = builder("Primary Alpha", 255).name("Alpha").bounds(0, 255).visible(b -> primaryMode.getValue().equals(Mode.RAINBOW)).build();

    public static Value<Mode> secondaryMode = builder("Secondary Mode", Mode.COLOR).name("Secondary").build();
    public static Value<Color> secondaryColor = builder("Secondary Color", new Color(50, 50, 50, 200)).name("Color").visible(b -> secondaryMode.getValue().equals(Mode.COLOR)).build();
    public static Value<Color> secondaryStart = builder("Secondary Start", Color.RED).name("Start").visible(b -> secondaryMode.getValue().equals(Mode.H_GRADIENT) || borderMode.getValue().equals(Mode.V_GRADIENT)).build();
    public static Value<Color> secondaryEnd = builder("Secondary End", Color.BLUE).name("End").visible(b -> secondaryMode.getValue().equals(Mode.H_GRADIENT) || borderMode.getValue().equals(Mode.V_GRADIENT)).build();
    public static Value<Integer> secondaryAlpha = builder("Secondary Alpha", 255).bounds(0, 255).visible(b -> secondaryMode.getValue().equals(Mode.RAINBOW)).build();

    public static Value<Mode> tertiaryMode = builder("Tertiary Mode", Mode.COLOR).name("Tertiary").build();
    public static Value<Color> tertiaryColor = builder("Tertiary Color", new Color(10, 10, 10, 200)).name("Color").visible(b -> tertiaryMode.getValue().equals(Mode.COLOR)).build();
    public static Value<Color> tertiaryStart = builder("Tertiary Start", Color.RED).name("Start").visible(b -> tertiaryMode.getValue().equals(Mode.H_GRADIENT) || borderMode.getValue().equals(Mode.V_GRADIENT)).build();
    public static Value<Color> tertiaryEnd = builder("Tertiary End", Color.BLUE).name("End").visible(b -> tertiaryMode.getValue().equals(Mode.H_GRADIENT) || borderMode.getValue().equals(Mode.V_GRADIENT)).build();
    public static Value<Integer> tertiaryAlpha = builder("Tertiary Alpha", 255).name("Alpha").bounds(0, 255).visible(b -> tertiaryMode.getValue().equals(Mode.RAINBOW)).build();

    public static Value<Boolean> textShadow = builder("Text Shadow", true).build();
    public static Value<Color> textColor = builder("Text Color", Color.WHITE).build();

    public enum Mode {
        COLOR, H_GRADIENT, V_GRADIENT, RAINBOW, TEXTURE
    }

    private static <T> ValueBuilder<T> builder(String name, T value) {
        return new ValueBuilder<>(name, value).list(values);
    }
}
