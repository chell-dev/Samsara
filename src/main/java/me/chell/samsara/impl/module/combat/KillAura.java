package me.chell.samsara.impl.module.combat;

import me.chell.samsara.api.event.PlayerUpdateEvent;
import me.chell.samsara.api.module.Module;
import me.chell.samsara.api.value.Value;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KillAura extends Module {
    public KillAura() {
        super("KillAura", Category.COMBAT);
    }

    private final Value<Boolean> sword = builder("Sword Only", false).build();
    private final Value<Float> range = builder("Range", 5.5f).bounds(0f, 6f).build();
    //private final Value<Boolean> rotate = builder("Rotate", true).build();
    //private final Value<Boolean> crits = builder("Criticals", true).build();
    private final Value<Boolean> delay = builder("Delay", true).build();

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if(sword.getValue() && !(getPlayer().getHeldItemMainhand().getItem() instanceof ItemSword)) return;

        List<EntityPlayer> list = new ArrayList<>();
        for(EntityPlayer player : getWorld().playerEntities) {
            if(player == getPlayer()) continue;
            if(getPlayer().getDistance(player) > range.getValue()) continue;
            if(player.getHealth() <= 0 || player.isDead) continue;
            list.add(player);
        }

        if(list.isEmpty()) return;
        list.sort(Comparator.comparing(player -> player.getDistance(getPlayer())));

        if(!delay.getValue() || getPlayer().getCooledAttackStrength(0f) >= 1f) {
            getPlayerController().attackEntity(getPlayer(), list.get(0));
            getPlayer().swingArm(EnumHand.MAIN_HAND);
        }

    }
}
