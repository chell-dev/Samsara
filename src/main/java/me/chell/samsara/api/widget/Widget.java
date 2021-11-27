package me.chell.samsara.api.widget;

import me.chell.samsara.api.Loadable;
import me.chell.samsara.api.gui.Drawable;
import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.util.Color;
import me.chell.samsara.api.util.Rainbow;
import me.chell.samsara.api.util.Wrapper;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.api.value.ValueBuilder;
import me.chell.samsara.impl.gui.ClickGUI;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class Widget extends Drawable implements Loadable {

    private final Value<Integer> xPos;
    private final Value<Integer> yPos;

    private final String name;
    private final Value<Boolean> enabled;
    private final List<Value<?>> values;

    private boolean grabbed = false;
    private int offsetX, offsetY;

    public Widget(String name, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.xPos = builder("X", x).bounds(0, 1).visible(b -> false).build();
        this.yPos = builder("Y", y).bounds(0, 1).visible(b -> false).build();
        this.name = name;
        this.enabled = builder("Enabled", false).build();
        this.values = new ArrayList<>();
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return xPos.getValue();
    }

    public int getY() {
        return yPos.getValue();
    }

    public boolean isEnabled() {
        return enabled.getValue();
    }

    public boolean setEnabled(boolean enabled) {
        return this.enabled.setValue(enabled);
    }

    public boolean toggle() {
        if(isEnabled()) {
            setEnabled(false);
            onDisable();
        } else {
            setEnabled(true);
            onEnable();
        }
        return isEnabled();
    }

    protected void onEnable(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    protected void onDisable(){
        MinecraftForge.EVENT_BUS.unregister(this);
    }
    public void render(){}

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Post event) {
        if(!isGuiOpen()) render();
    }

    protected static boolean isGuiOpen() {
        return Wrapper.getMinecraft().currentScreen instanceof ClickGUI;
    }

    public List<Value<?>> getValues() {
        return values;
    }

    @SuppressWarnings("unchecked")
    public <T> Value<T> getValue(String name) {
        for(Value<?> v : values) {
            if(v.getName().equalsIgnoreCase(name)) return (Value<T>)v;
        }
        return null;
    }

    public <T> ValueBuilder<T> builder(String name, T value) {
        return new ValueBuilder<>(name, value).list(values);
    }

    private int background = new Color(10, 10, 10, 100).getARGB();

    @Override
    public void draw(int mouseX, int mouseY) {
        if(grabbed) {
            xPos.setValue(mouseX - offsetX);
            yPos.setValue(mouseY - offsetY);
        }

        ScaledResolution res = Wrapper.getScaledResolution();
        if(xPos.getValue() < 0) xPos.setValue(0);
        if(yPos.getValue() < 0) yPos.setValue(0);
        if(xPos.getValue() + width > res.getScaledWidth()) xPos.setValue(res.getScaledWidth() - width);
        if(yPos.getValue() + height > res.getScaledHeight()) yPos.setValue(res.getScaledHeight() - height);

        drawRect(xPos.getValue(), yPos.getValue(), width, height, background);

        render();
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        return super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= xPos.getValue() && mouseX <= xPos.getValue() + width && mouseY >= yPos.getValue() && mouseY <= yPos.getValue() + height) {
            if(mouseButton == 0) {
                offsetX = mouseX - xPos.getValue();
                offsetY = mouseY - yPos.getValue();
                grabbed = true;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if(state == 0) grabbed = false;
    }

    @Override
    public void onGuiClosed() {
        grabbed = false;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    protected void drawString(String text, int x, int y, int width, int height, boolean shadow, Align hAlign, ColorMode color, int customColor) {
        int c = getStringColor(color, customColor).getARGB();
        int drawX = x;
        if (hAlign == Align.RIGHT) {
            drawX = x - Wrapper.getFontRenderer().getStringWidth(text);
        } else if (hAlign == Align.CENTER) {
            drawX = x - Wrapper.getFontRenderer().getStringWidth(text) / 2;
        }
        Wrapper.getFontRenderer().drawString(text, drawX, y, c, shadow);
    }

    protected Color getStringColor(ColorMode color, int customColor) {
        switch (color) {
            case PRIMARY:
                switch (GuiTheme.primaryMode.getValue()) {
                    case RAINBOW:
                        return Rainbow.getColor(255);
                    case COLOR:
                    case TEXTURE:
                        return GuiTheme.primaryColor.getValue();
                    case H_GRADIENT:
                    case V_GRADIENT:
                        return GuiTheme.primaryStart.getValue();
                }
                break;
            case SECONDARY:
                switch (GuiTheme.secondaryMode.getValue()) {
                    case RAINBOW:
                        return Rainbow.getColor(255);
                    case COLOR:
                    case TEXTURE:
                        return GuiTheme.secondaryColor.getValue();
                    case H_GRADIENT:
                    case V_GRADIENT:
                        return GuiTheme.secondaryStart.getValue();
                }
                break;
            case TERTIARY:
                switch (GuiTheme.tertiaryMode.getValue()) {
                    case RAINBOW:
                        return Rainbow.getColor(255);
                    case COLOR:
                    case TEXTURE:
                        return GuiTheme.tertiaryColor.getValue();
                    case H_GRADIENT:
                    case V_GRADIENT:
                        return GuiTheme.tertiaryStart.getValue();
                }
                break;
            case TEXT:
                return GuiTheme.textColor.getValue();
            case RAINBOW:
                return Rainbow.getColor(255);
            case CUSTOM:
                return new Color(customColor);
        }
        return Color.WHITE();
    }

    public enum Align {
        LEFT, CENTER, RIGHT
    }

    public enum ColorMode {
        PRIMARY, SECONDARY, TERTIARY, TEXT, RAINBOW, CUSTOM
    }
}
