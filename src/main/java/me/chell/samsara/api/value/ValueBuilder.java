package me.chell.samsara.api.value;

import java.util.function.Predicate;

public class ValueBuilder<T> {
    private final String name;
    private final T value;
    private T sliderMin;
    private T sliderMax;
    private Predicate<Boolean> visible;

    public ValueBuilder(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public ValueBuilder<T> bounds(T min, T max) {
        sliderMin = min;
        sliderMax = max;
        return this;
    }

    public ValueBuilder<T> visible(Predicate<Boolean> predicate) {
        visible = predicate;
        return this;
    }

    public Value<T> build() {
        if(visible == null) visible = b -> true;
        return new Value<>(name, value, sliderMin, sliderMax, visible);
    }
}
