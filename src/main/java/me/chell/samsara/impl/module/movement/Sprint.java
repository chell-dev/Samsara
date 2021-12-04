package me.chell.samsara.impl.module.movement;

import me.chell.samsara.api.event.PlayerUpdateEvent;
import me.chell.samsara.api.module.Module;
import me.chell.samsara.api.value.Value;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Category.MOVEMENT);
    }

    private final Value<Boolean> strict = builder("Strict", true).build();

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if(strict.getValue() && getPlayer().moveForward <= 0) return;
        getPlayer().setSprinting(true);
    }
}
