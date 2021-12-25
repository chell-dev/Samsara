package me.chell.samsara.api.gui;

import me.chell.samsara.api.util.WrapperJava;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class Drawable implements WrapperJava {
    public int x, y, width, height;

    protected final MatrixStack matrices = new MatrixStack();

    public Drawable(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY) {
    }

    public boolean keyTyped(char typedChar, int keyCode) {
        return false;
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

    public void onGuiClosed() {
    }

    public void updateScreen() {
    }

    public void drawThemedString(String text, int x, int y) {
       getFontRenderer().drawWithShadow(matrices, text, x, y, GuiTheme.textColor.getValue().getArgb());
    }

    public void drawThemedStringRight(String text, int x, int y) {
        x -= getFontRenderer().getWidth(text);
        getFontRenderer().drawWithShadow(matrices, text, x, y, GuiTheme.textColor.getValue().getArgb());
    }

    public int getStringCenterX(int start, int end, String text) {
        return start + end/2 - getFontRenderer().getWidth(text)/2;
    }

    public int getStringCenterY(int start, int end) {
        return start + end/2 - getFontRenderer().fontHeight/2;
    }

    public void drawThemedRectPrimary(int x, int y, int width, int height) {
        drawRect(x, y, width, height, GuiTheme.primaryColor.getValue().getArgb());
    }

    public void drawThemedRectSecondary(int x, int y, int width, int height) {
        drawRect(x, y, width, height, GuiTheme.secondaryColor.getValue().getArgb());
    }

    public void drawThemedRectTertiary(int x, int y, int width, int height) {
        drawRect(x, y, width, height, GuiTheme.tertiaryColor.getValue().getArgb());
    }

    public void drawThemedBorder(int x, int y, int width, int height) {
        drawBorder(x, y, width, height, GuiTheme.borderColor.getValue().getArgb(), GuiTheme.borderSize.getValue());
    }

    public void drawBorder(int x, int y, int width, int height, int color, int size) {
        drawRect(x, y - size, width, size, color); // top
        drawRect(x, y + height, width, size, color); // bottom
        drawRect(x - size, y - size, size, height + size+size, color); // left
        drawRect(x + width, y - size, size, height + size+size, color); // right
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        DrawableHelper.fill(matrices, x, y, x + width, y + height, color);
    }

    public void drawTexturedRect(int x, int y, int width, int height) {
        DrawableHelper.drawTexture(matrices, x, y, width, height, 0f, 0f, width, height, width, height);
    }
}
