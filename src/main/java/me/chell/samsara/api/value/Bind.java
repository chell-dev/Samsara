package me.chell.samsara.api.value;

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
}
