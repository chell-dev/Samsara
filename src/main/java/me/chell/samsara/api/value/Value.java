package me.chell.samsara.api.value;

import java.util.function.Predicate;

public class Value<T> {
    private final String name;
    private final T defaultValue;
    private T value;
    private T sliderMin;
    private T sliderMax;
    private Predicate<Boolean> visible;

    public Value(String name, T value, T sliderMin, T sliderMax, Predicate<Boolean> visible) {
        this.name = name;
        this.defaultValue = this.value = value;
        this.sliderMin = sliderMin;
        this.sliderMax = sliderMax;
        this.visible = visible;
    }

    public String getName() {
        return name;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public T getValue() {
        return value;
    }

    public T setValue(T value) {
        return this.value = value;
    }

    public T getSliderMin() {
        return sliderMin;
    }

    public T getSliderMax() {
        return sliderMax;
    }

    public T setSliderMin(T sliderMin) {
        return this.sliderMin = sliderMin;
    }

    public T setSliderMax(T sliderMax) {
        return this.sliderMax = sliderMax;
    }

    public Predicate<Boolean> getVisibility() {
        return visible;
    }

    public Predicate<Boolean> setVisibility(Predicate<Boolean> visible) {
        return this.visible = visible;
    }

    public boolean isVisible() {
        return visible.test(false);
    }

}
