package me.chell.samsara.impl.gui;

import me.chell.samsara.api.gui.Drawable;
import me.chell.samsara.api.value.Value;

public class ValueButton<T> extends Drawable {
    protected final Value<T> value;

    public ValueButton(int x, int y, int width, int height, Value<T> value) {
        super(x, y, width, height);
        this.value = value;
    }

    public boolean isVisible() {
        return value.isVisible();
    }
}
