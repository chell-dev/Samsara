package me.chell.samsara.impl.module.movement;

import me.chell.samsara.api.module.Module;
import me.chell.samsara.api.util.Wrapper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(Wrapper.getPlayer() != null && Wrapper.getPlayer().moveForward > 0)
            Wrapper.getPlayer().setSprinting(true);
    }
}
