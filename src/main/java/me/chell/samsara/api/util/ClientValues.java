package me.chell.samsara.api.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.api.value.ValueBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientValues implements Wrapper {
    public static List<Value<?>> values = new ArrayList<>();

    private static ClientValues INSTANCE;

    public ClientValues() {
        INSTANCE = this;
    }

    private static final Value<Object> messageBlank = builder("Client Messages", null).build();
    public static final Value<ChatFormatting> msgPrefixColor = builder("Prefix Color", ChatFormatting.DARK_PURPLE).build();
    public static final Value<ChatFormatting> msgColor = builder("Text Color", ChatFormatting.GRAY).build();
    public static final Value<Boolean> msgDelete = builder("Delete", false).build();

    private static final Value<Object> otherBlank = builder("Other", null).build();
    public static final Value<Character> cmdPrefix = builder("Prefix", ',').build();
    public static final Value<Integer> rainbowSpeed = builder("RgbSpeed", 2).bounds(1, 5).build();
    public static final Value<Boolean> fovOverride = builder("Set FOV", false).build();
    private final Value<Integer> fovValue = new Value<Integer>("FOV", 130, 110, 180, b -> fovOverride.getValue()) {
        @Override
        public void init() {
            values.add(this);
        }

        @Override
        public Integer setValue(Integer value) {
            getGameSettings().fovSetting = (float)value;
            return super.setValue(value);
        }
    };

    public static int getFOV() {
        return INSTANCE.fovValue.getValue();
    }

    public static Value<?> getValue(String name) {
        for(Value<?> value : values) {
            if(name.equalsIgnoreCase(value.getName())) return value;
        }
        return null;
    }

    private static <T> ValueBuilder<T> builder(String name, T value) {
        return new ValueBuilder<>(name, value).list(values);
    }
}
