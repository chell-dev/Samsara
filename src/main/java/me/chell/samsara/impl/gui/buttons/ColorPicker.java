package me.chell.samsara.impl.gui.buttons;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.chell.samsara.api.gui.Drawable;
import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.util.Color;
import me.chell.samsara.api.util.Wrapper;
import me.chell.samsara.api.value.Value;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.awt.Toolkit;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ColorPicker extends Drawable {
    private Value<Color> value;

    private final ResourceLocation texture;
    private BufferedImage image;

    private boolean open = false;
    private boolean mouseDown = false;
    private int buttonHeight = 12;
    private boolean sliderGrabbed;

    private List<Textbox> textBoxes;

    public ColorPicker(Value<Color> value, int x, int y) {
        super(x, y, GuiTheme.width, 100);
        this.value = value;

        texture = new ResourceLocation("samsara", "textures/colorpicker.png");

        // https://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
        try {
            InputStream is = Wrapper.getMinecraft().getResourceManager().getResource(texture).getInputStream();
            image = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Color c = value.getValue();
        int w = Wrapper.getFontRenderer().getStringWidth("000")+2;
        int h = Wrapper.getFontRenderer().FONT_HEIGHT+2;
        textBoxes = Lists.newArrayList(
                new Textbox(x, y, w, h, "", ""+c.getRed()),
                new Textbox(x, y, w, h, "", ""+c.getGreen()),
                new Textbox(x, y, w, h, "", ""+c.getBlue()),
                new Textbox(x, y, w, h, "", ""+c.getAlpha())
        );
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if(!value.isVisible()) {
            height = 0;
            return;
        }

        if(open) {
            height = 112;

            drawThemedRectPrimary(x+2, y, width-2, buttonHeight-1);

            Color c = value.getValue();

            int startX = x;
            int startY = y + buttonHeight + 1;

            drawThemedRectSecondary(x+2, y + buttonHeight, width-2, height - buttonHeight);

            textBoxes.get(0).setValue(""+c.getRed());
            textBoxes.get(1).setValue(""+c.getGreen());
            textBoxes.get(2).setValue(""+c.getBlue());
            textBoxes.get(3).setValue(""+c.getAlpha());

            int boxX = startX + 6;
            for(Textbox box : textBoxes) {
                box.setX(boxX);
                box.setY(startY + 86);
                box.draw();
                boxX += 27;
            }

            int w = Wrapper.getFontRenderer().getStringWidth("000");
            drawGradientRectVertical(startX + 88, startY + 1, w, 82, 0xff000000, 0x00000000);
            drawBorder(startX + 88, startY + 1, w, 82, GuiTheme.tertiaryColor.getValue().getARGB(), 1);

            int imgSize = 84;

            Wrapper.getMinecraft().getTextureManager().bindTexture(texture);
            GlStateManager.color(1f, 1f, 1f, 1f);
            drawTexturedRect(startX+2, startY, imgSize, imgSize);

            if(mouseDown && image != null) {
                float scale = (image.getWidth() / (float)imgSize);
                int mX = MathHelper.clamp(mouseX - (startX+2), 0, imgSize-1);
                int mY = MathHelper.clamp(mouseY - startY, 0, imgSize-1);

                int color = image.getRGB((int)(mX * scale), (int)(mY * scale));
                value.getValue().setRGB(color);
            }

            if(sliderGrabbed) { // TODO fix the weird shit with text boxes
                int sliderY = startY + 1;
                int sliderHeight = 82;
                float mousePos = MathHelper.clamp(mouseY - sliderY, 0, sliderHeight);
                float val = mousePos / sliderHeight;

                value.getValue().setAlpha((int)(val*255));
            }
        } else {
            height = 12;
            drawThemedRectSecondary(x+2, y, width-2, buttonHeight-1);
        }
        drawThemedRectTertiary(x, y, 2, height);
        drawThemedRectTertiary(x+2, y+buttonHeight-1, width-2, 1);

        drawThemedString(value.getDisplayName(), x + 4, y + (buttonHeight-1)/2 - 4);
        drawThemedStringRight(ChatFormatting.GRAY+Integer.toHexString(value.getValue().getARGB()), x + width - 2, y + (buttonHeight-1)/2 - 4); // TODO draw a small ractangle
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0 && mouseX >= x+2 && mouseY >= y) {
            if(mouseX <= x+width && mouseY <= y + buttonHeight) {
                open = !open;
                return true;
            }
            else if(open && mouseX <= x+2 + 84 && mouseY >= y+buttonHeight+1 && mouseY <= y+buttonHeight+1 + 84) {
                mouseDown = true;
                return true;
            } else if(open && mouseX >= x + 88 && mouseY >= y+buttonHeight+1 + 1 && mouseX <= x+88 + Wrapper.getFontRenderer().getStringWidth("000") && mouseY <= y+buttonHeight+2 + 82) {
                //drawGradientRectVertical(startX + 88, startY + 1, w, 82, 0xff000000, 0x00000000);
                sliderGrabbed = true;
                return true;
            }
        }

        if(open) {
            for(Textbox box : textBoxes) {
                if(box.mouseClicked(mouseX, mouseY)) return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if(state == 0) {
            mouseDown = false;
            sliderGrabbed = false;
        }
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        for(Textbox box : textBoxes) {
            switch (box.keyTyped(typedChar, keyCode)) {
                case 0:
                    break;
                case 1:
                    return true;
                case 2:
                    int v;
                    try {
                        v = Integer.parseInt(box.getEditText());
                    } catch (NumberFormatException e) {
                        box.setEditText(box.getValue());
                        return true;
                    }
                    if(v > 255 || v < 0) {
                        box.setEditText(box.getValue());
                        return true;
                    }
                    switch (textBoxes.indexOf(box)) {
                        case 0:
                            value.getValue().setRed(v);
                            break;
                        case 1:
                            value.getValue().setGreen(v);
                            break;
                        case 2:
                            value.getValue().setBlue(v);
                            break;
                        case 3:
                            value.getValue().setAlpha(v);
                            break;
                    }
                    box.setEditText(box.getValue());
                    return true;
            }
        }
        return super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        mouseDown = false;
        sliderGrabbed = false;
        for(Textbox box : textBoxes) {
            box.setListening(false);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    private class Textbox {
        private int x;
        private int y;
        private int width;
        private int height;
        private String title;
        private String value;
        private int strWidth;

        private boolean listening = false;
        private String editText;

        public Textbox(int x, int y, int width, int height, String title, String value) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.title = title;
            this.value = editText = value;
            strWidth = Wrapper.getFontRenderer().getStringWidth(title);
        }

        public void draw() {
            drawThemedString(title, x - strWidth - 2, y);
            drawBorder(x, y, width, height, GuiTheme.tertiaryColor.getValue().getARGB(), 1);
            drawThemedString(listening ? editText : value, x+1, y+2);
        }

        public boolean mouseClicked(int mouseX, int mouseY) {
            if(mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
                listening = !listening;
                editText = value;
                System.out.println("textbox mouse clicked");
                return true;
            }
            return false;
        }

        private final List<Character> numbers = Lists.newArrayList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

        public int keyTyped(char typedChar, int keyCode) {
            if(listening) {
                switch (keyCode) {
                    case Keyboard.KEY_BACK:
                        editText = StringUtils.chop(editText);
                        break;
                    case Keyboard.KEY_RETURN:
                        listening = false;
                        return 2;
                    case Keyboard.KEY_ESCAPE:
                        listening = false;
                        editText = value;
                        break;
                    case Keyboard.KEY_V:
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            editText += Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
                        }
                        break;
                    default:
                        if(numbers.contains(typedChar)) {
                            editText += typedChar;
                        }
                }
                return 1;
            }
            return 0;
        }

        public String getEditText() {
            return editText;
        }

        public String getValue() {
            return value;
        }

        public void setEditText(String editText) {
            this.editText = editText;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setListening(boolean listening) {
            this.listening = listening;
        }
    }
}
