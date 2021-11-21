package me.chell.samsara.impl.module.combat;

import me.chell.samsara.api.module.Module;
import me.chell.samsara.api.value.Value;

public class CombatModule extends Module {
    public CombatModule() {
        super("CombatModule", Category.COMBAT);
    }

    private Value<Boolean> value1 = builder("Value1", false).build();
    private Value<Boolean> value2 = builder("Value2", false).build();
    private Value<Boolean> value3 = builder("Value3", false).build();

}
