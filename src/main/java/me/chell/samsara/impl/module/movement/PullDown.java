package me.chell.samsara.impl.module.movement;

import me.chell.samsara.api.event.PlayerUpdateEvent;
import me.chell.samsara.api.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PullDown extends Module {
    public PullDown() {
        super("PullDown", Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (getPlayer().onGround && getPlayer().isOffsetPositionInLiquid(0D, 0D, 0D)) // no the liquid check shouldn't be inverted
            --getPlayer().motionY;
    }
}
