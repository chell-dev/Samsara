package me.chell.samsara.api.value;

import java.util.List;
import java.util.function.Predicate;

public class ValueBuilder<T> {
    private final String name;
    private final T value;
    private T sliderMin;
    private T sliderMax;
    private Predicate<Boolean> visible;
    private List<Value<?>> addTo;

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

    public ValueBuilder<T> list(List<Value<?>> addTo) {
        this.addTo = addTo;
        return this;
    }

    public Value<T> build() {
        if(visible == null) visible = b -> true;
        Value<T> v = new Value<>(name, value, sliderMin, sliderMax, visible);
        if(addTo != null) addTo.add(v);
        return v;
    }
}
