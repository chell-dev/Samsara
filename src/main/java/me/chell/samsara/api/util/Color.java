package me.chell.samsara.api.util;

public class Color {
    private int red;
    private int green;
    private int blue;
    private int alpha;

    public Color(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(int argb) {
        setARGB(argb);
    }

    public Color(int rgb, int alpha) {
        setRGB(rgb);
        setAlpha(alpha);
    }

    public int getARGB() {
        return ((alpha & 0xFF) << 24) |
                ((red & 0xFF) << 16) |
                ((green & 0xFF) << 8) |
                ((blue & 0xFF));
    }

    public void setRGB(int argb) {
        red = (argb >> 16) & 0xFF;
        green = (argb >> 8) & 0xFF;
        blue = (argb) & 0xFF;
    }

    public void setARGB(int argb) {
        alpha = (argb >> 24) & 0xFF;
        setRGB(argb);
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    @Override
    public String toString() {
        return ""+getARGB();
    }

    public static Color WHITE = new Color(255, 255, 255, 255);
    public static Color BLACK = new Color(0, 0, 0, 255);
    public static Color RED = new Color(255, 0, 0, 255);
    public static Color GREEN = new Color(0, 255, 0, 255);
    public static Color BLUE = new Color(0, 0, 255, 255);
}
