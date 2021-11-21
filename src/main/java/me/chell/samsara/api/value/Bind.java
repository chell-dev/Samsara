package me.chell.samsara.api.value;

import org.lwjgl.input.Keyboard;

public class Bind {
    private int key;

    public Bind(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public int setKey(int key) {
        return this.key = key;
    }

    @Override
    public String toString() {
        return Keyboard.getKeyName(getKey());
    }
}
