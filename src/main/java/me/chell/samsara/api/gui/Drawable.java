package me.chell.samsara.api.gui;

import me.chell.samsara.api.util.Rainbow;
import me.chell.samsara.api.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class Drawable {
    public int x, y, width, height;

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
        Minecraft.getMinecraft().fontRenderer.drawString(text, x, y, GuiTheme.textColor.getValue().getARGB(), GuiTheme.textShadow.getValue());
    }

    public void drawThemedStringRight(String text, int x, int y) {
        x -= Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
        Minecraft.getMinecraft().fontRenderer.drawString(text, x, y, GuiTheme.textColor.getValue().getARGB(), GuiTheme.textShadow.getValue());
    }

    public int getStringCenterX(int start, int end, String text) {
        return start + end/2 - Wrapper.getFontRenderer().getStringWidth(text)/2;
    }

    public int getStringCenterY(int start, int end) {
        return start + end/2 - Wrapper.getFontRenderer().FONT_HEIGHT/2;
    }

    public void drawThemedRectPrimary(int x, int y, int width, int height) {
        switch (GuiTheme.primaryMode.getValue()) {
            case COLOR:
                int color = GuiTheme.primaryColor.getValue().getARGB();
                drawRect(x, y, width, height, color);
                break;
            case RAINBOW:
                drawRect(x, y, width, height, Rainbow.getARGB(GuiTheme.primaryAlpha.getValue()));
                break;
            case H_GRADIENT:
                drawGradientRectHorizontal(x, y, width, height, GuiTheme.primaryStart.getValue().getARGB(), GuiTheme.primaryEnd.getValue().getARGB());
                break;
            case V_GRADIENT:
                drawGradientRectVertical(x, y, width, height, GuiTheme.primaryStart.getValue().getARGB(), GuiTheme.primaryEnd.getValue().getARGB());
                break;
            case TEXTURE:
                Minecraft.getMinecraft().getTextureManager().bindTexture(GuiTheme.primaryTex);
                GlStateManager.color(1f, 1f, 1f, 1f);
                drawTexturedRect(x, y, width, height);
                break;
        }
    }

    public void drawThemedRectSecondary(int x, int y, int width, int height) {
        switch (GuiTheme.secondaryMode.getValue()) {
            case COLOR:
                int color = GuiTheme.secondaryColor.getValue().getARGB();
                drawRect(x, y, width, height, color);
                break;
            case RAINBOW:
                drawRect(x, y, width, height, Rainbow.getARGB(GuiTheme.secondaryAlpha.getValue()));
                break;
            case H_GRADIENT:
                drawGradientRectHorizontal(x, y, width, height, GuiTheme.secondaryStart.getValue().getARGB(), GuiTheme.secondaryEnd.getValue().getARGB());
                break;
            case V_GRADIENT:
                drawGradientRectVertical(x, y, width, height, GuiTheme.secondaryStart.getValue().getARGB(), GuiTheme.secondaryEnd.getValue().getARGB());
                break;
            case TEXTURE:
                Minecraft.getMinecraft().getTextureManager().bindTexture(GuiTheme.secondaryTex);
                GlStateManager.color(1f, 1f, 1f, 1f);
                drawTexturedRect(x, y, width, height);
                break;
        }
    }

    public void drawThemedRectTertiary(int x, int y, int width, int height) {
        switch (GuiTheme.tertiaryMode.getValue()) {
            case COLOR:
                int color = GuiTheme.tertiaryColor.getValue().getARGB();
                drawRect(x, y, width, height, color);
                break;
            case RAINBOW:
                drawRect(x, y, width, height, Rainbow.getARGB(GuiTheme.tertiaryAlpha.getValue()));
                break;
            case H_GRADIENT:
                drawGradientRectHorizontal(x, y, width, height, GuiTheme.tertiaryStart.getValue().getARGB(), GuiTheme.tertiaryEnd.getValue().getARGB());
                break;
            case V_GRADIENT:
                drawGradientRectVertical(x, y, width, height, GuiTheme.tertiaryStart.getValue().getARGB(), GuiTheme.tertiaryEnd.getValue().getARGB());
                break;
            case TEXTURE:
                Minecraft.getMinecraft().getTextureManager().bindTexture(GuiTheme.tertiaryTex);
                GlStateManager.color(1f, 1f, 1f, 1f);
                drawTexturedRect(x, y, width, height);
                break;
        }
    }

    public void drawThemedBorder(int x, int y, int width, int height) {
        int border = GuiTheme.borderSize.getValue();
        if(border == 0) return;
        switch (GuiTheme.borderMode.getValue()) {
            case COLOR:
                drawBorder(x, y, width, height, GuiTheme.borderColor.getValue().getARGB(), GuiTheme.borderSize.getValue());
                break;
            case RAINBOW:
                drawBorder(x, y, width, height, Rainbow.getARGB(GuiTheme.borderAlpha.getValue()), GuiTheme.borderSize.getValue());
                break;
            case H_GRADIENT:
                drawGradientRectHorizontal(x, y - border, width, border, GuiTheme.borderStart.getValue().getARGB(), GuiTheme.borderEnd.getValue().getARGB());
                drawGradientRectHorizontal(x, y + height, width, border, GuiTheme.borderStart.getValue().getARGB(), GuiTheme.borderEnd.getValue().getARGB());
                drawGradientRectHorizontal(x - border, y - border, border, height + border+border, GuiTheme.borderStart.getValue().getARGB(), GuiTheme.borderEnd.getValue().getARGB());
                drawGradientRectHorizontal(x + width, y - border, border, height + border+border, GuiTheme.borderStart.getValue().getARGB(), GuiTheme.borderEnd.getValue().getARGB());
                break;
            case V_GRADIENT:
                drawGradientRectVertical(x, y - border, width, border, GuiTheme.borderStart.getValue().getARGB(), GuiTheme.borderEnd.getValue().getARGB());
                drawGradientRectVertical(x, y + height, width, border, GuiTheme.borderStart.getValue().getARGB(), GuiTheme.borderEnd.getValue().getARGB());
                drawGradientRectVertical(x - border, y - border, border, height + border+border, GuiTheme.borderStart.getValue().getARGB(), GuiTheme.borderEnd.getValue().getARGB());
                drawGradientRectVertical(x + width, y - border, border, height + border+border, GuiTheme.borderStart.getValue().getARGB(), GuiTheme.borderEnd.getValue().getARGB());
                break;
            case TEXTURE:
                int size = GuiTheme.borderSize.getValue();

                Minecraft.getMinecraft().getTextureManager().bindTexture(GuiTheme.borderTex);
                GlStateManager.color(1f, 1f, 1f, 1f);
                drawTexturedRect(x, y - size, width, size);
                drawTexturedRect(x, y + height, width, size);
                drawTexturedRect(x - size, y - size, size, height + size+size);
                drawTexturedRect(x + width, y - size, size, height + size+size);
                break;
        }
    }

    public void drawBorder(int x, int y, int width, int height, int color, int size) {
        drawRect(x, y - size, width, size, color); // top
        drawRect(x, y + height, width, size, color); // bottom
        drawRect(x - size, y - size, size, height + size+size, color); // left
        drawRect(x + width, y - size, size, height + size+size, color); // right
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    public void drawTexturedRect(int x, int y, int width, int height) {
        Gui.drawScaledCustomSizeModalRect(x, y, 0, 0, width, height, width, height, (float)width, (float) height);
    }

    public void drawGradientRectHorizontal(int x, int y, int width, int height, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, 0.0D).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x, y + height, 0.0D).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0D).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(x + width, y, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void drawGradientRectVertical(int x, int y, int width, int height, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y + height, 0.0D).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0D).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x + width, y, 0.0D).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(x, y, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
