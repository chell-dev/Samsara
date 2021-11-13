package me.chell.samsara.api.module;

import com.google.common.collect.Lists;
import me.chell.samsara.api.Loadable;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.api.value.ValueBuilder;

import java.util.ArrayList;
import java.util.List;

public class Module implements Loadable {
    private final String name;
    private final Value<String> displayName;
    private final Value<Boolean> enabled;
    private final Value<Integer> keyBind; // TODO: create KeyBind class
    private final Category category;
    private final List<Value<?>> values;

    public Module(String name, Category category, List<Value<?>> values) {
        this.name = name;
        this.displayName = new ValueBuilder<>("DisplayName", name).visible(b -> false).build();
        this.enabled = new ValueBuilder<>("Enabled", false).visible(b -> false).build();
        this.keyBind = new ValueBuilder<>("Bind", 0).build();
        this.category = category;
        this.values = Lists.newArrayList(displayName, enabled);
        this.values.addAll(values);
    }

    public Module(String name, Category category) {
        this(name, category, new ArrayList<>());
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
        return keyBind.getValue();
    }

    public int setBind (int key) {
        return keyBind.setValue(key);
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

    public void onEnable() {}
    public void onDisable() {}

    @Override
    public void load() {} // TODO

    @Override
    public void unload() {} // TODO

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
