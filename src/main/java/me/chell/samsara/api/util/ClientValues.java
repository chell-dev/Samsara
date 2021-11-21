package me.chell.samsara.api.util;

import me.chell.samsara.api.value.Value;
import me.chell.samsara.api.value.ValueBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClientValues {
    public static List<Value<?>> values = new ArrayList<>();

    public static Value<Character> cmdPrefix = builder("Prefix", ',').build();
    public static Value<Integer> rainbowSpeed = builder("RgbSpeed", 2).bounds(1, 5).build();
    public static Value<Boolean> fovOverride = builder("Set FOV", false).build();
    public static Value<Integer> fovValue = new Value<Integer>("FOV", 130, 110, 180, b -> fovOverride.getValue()) {
        @Override
        public void init() {
            values.add(this);
        }

        @Override
        public Integer setValue(Integer value) {
            Wrapper.getGameSettings().fovSetting = (float)value;
            return super.setValue(value);
        }
    };

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
