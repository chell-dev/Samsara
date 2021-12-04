package me.chell.samsara.impl.module.misc;

import me.chell.samsara.api.event.PlayerUpdateEvent;
import me.chell.samsara.api.module.Module;
import me.chell.samsara.api.value.Value;
import me.chell.samsara.impl.mixin.MinecraftAccessor;
import me.chell.samsara.impl.mixin.PlayerControllerMPAccessor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastUse extends Module {
    public FastUse() {
        super("FastUse", Category.MISC);
    }

    private final Value<Boolean> smart = builder("Whitelist", true).build();

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        ((PlayerControllerMPAccessor)getPlayerController()).setBlockHitDelay(0);

        if(!smart.getValue() || mainHand() || offHand()) ((MinecraftAccessor)getMinecraft()).setRightClickDelayTimer(0);
    }

    private boolean mainHand() {
        return getPlayer().getHeldItemMainhand().getItem() instanceof ItemExpBottle
                || getPlayer().getHeldItemMainhand().getItem() instanceof ItemEndCrystal;
    }

    private boolean offHand() {
        boolean item = getPlayer().getHeldItemOffhand().getItem() instanceof ItemExpBottle
                || getPlayer().getHeldItemOffhand().getItem() instanceof ItemEndCrystal;
        return item && !(getPlayer().getHeldItemMainhand().getItem() instanceof ItemBlock);
    }
}
