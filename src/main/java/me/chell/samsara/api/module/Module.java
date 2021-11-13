package me.chell.samsara.api.module;

import me.chell.samsara.api.Loadable;
import me.chell.samsara.api.value.Bind;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.api.value.ValueBuilder;

import java.util.ArrayList;
import java.util.List;

public class Module implements Loadable {
    private final String name;
    private final Value<String> displayName;
    private final Value<Boolean> enabled;
    private final Value<Bind> bind;
    private final Category category;
    private final List<Value<?>> values;

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.values = new ArrayList<>();
        this.displayName = builder("DisplayName", name).visible(b -> false).build();
        this.enabled = builder("Enabled", false).visible(b -> false).build();
        this.bind = builder("Bind", new Bind(0)).build();
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName.getValue();
    }

    public String setDisplayName(String value) {
        return displayName.setValue(value);
    }

    public boolean isEnabled() {
        return enabled.getValue();
    }

    public boolean setEnabled(boolean enabled) {
        return this.enabled.setValue(enabled);
    }

    public int getBind() {
        return bind.getValue().getKey();
    }

    public int setBind (int key) {
        return bind.getValue().setKey(key);
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

    public Category getCategory() {
        return category;
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

    public void onEnable() {}
    public void onDisable() {}

    @Override
    public void load() {}

    @Override
    public void unload() {
        if(isEnabled()) toggle();
    }

    public enum Category {
        COMBAT(new ValueBuilder<>("DisplayName", "COMBAT").visible(b -> false).build()),
        MISC(new ValueBuilder<>("DisplayName", "MISC").visible(b -> false).build());

        private final me.chell.samsara.api.value.Value<String> displayName;
        Category(Value<String> displayName) {
            this.displayName = displayName;
        }

        public String getName() {
            return displayName.getValue();
        }

        public String setName(String value) {
            return displayName.setValue(value);
        }
    }
}
