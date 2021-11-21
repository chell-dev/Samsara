package me.chell.samsara.impl.module.misc;

import me.chell.samsara.Samsara;
import me.chell.samsara.api.module.Module;
import me.chell.samsara.api.util.Color;
import me.chell.samsara.api.value.Value;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class TestModule extends Module {
    public TestModule() {
        super("MiscModule", Category.MISC);
        setBind(Keyboard.KEY_P);
    }

    private Value<Boolean> booleanVal = builder("Boolean", false).build();
    private Value<Object> blankVal = builder("Blank", null).build();
    private Value<Character> charVal = builder("Character", '>').build();
    private Value<Test> enumVal = builder("Enum", Test.ONE).build();
    private Value<Integer> intVal = builder("Integer", 11).bounds(9, 20).build();
    private Value<Double> doubleVal = builder("Double", 5d).bounds(0d, 10d).build();
    private Value<Float> floatVal = builder("Float", 5f).bounds(0f, 10f).build();
    private Value<Color> colorVal = builder("Color", Color.RED).build();

    @Override
    public void onEnable() {
        super.onEnable();
        Minecraft.getMinecraft().displayGuiScreen(Samsara.INSTANCE.clickGUI);
        toggle();
    }

    private enum Test {
        ONE, TWO, THREE
    }
}
