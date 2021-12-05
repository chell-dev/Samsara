package me.chell.samsara.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerKilledEvent extends Event {
    private final EntityPlayer target;

    public PlayerKilledEvent(EntityPlayer target) {
        this.target = target;
    }

    public EntityPlayer getTarget() {
        return target;
    }
}
