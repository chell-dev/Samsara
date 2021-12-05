package me.chell.samsara.api.util;

import me.chell.samsara.api.Loadable;
import me.chell.samsara.api.event.PlayerKilledEvent;
import me.chell.samsara.api.event.PlayerUpdateEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class KillEventManager implements Loadable, Wrapper { // add to autocrystal // maybe put this in separate forge mod for kd tracker
    private final HashMap<EntityPlayer, Integer> targets = new HashMap<>();

    @Override
    public void load() {
        MinecraftForge.EVENT_BUS.register(this);
        targets.clear();
    }

    @Override
    public void unload() {
        MinecraftForge.EVENT_BUS.unregister(this);
        targets.clear();
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.getEntityPlayer() != getPlayer()) return;

        Entity target = event.getTarget();

        if (target instanceof EntityPlayer) {
            targets.put((EntityPlayer) target, 60);
        } else if (target instanceof EntityEnderCrystal) {
            for (EntityPlayer player : getPlayersInRange(target.getPosition(), 10d)) {
                targets.put(player, 60);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        List<EntityPlayer> remove = new ArrayList<>();

        targets.forEach(((player, timeout) -> {
            if(timeout <= 0) {
                remove.add(player);
                return;
            }

            if(player.isDead || player.getHealth() <= 0f) {
                remove.add(player);
                playerKilled(player);
                return;
            }

            targets.put(player, timeout-1);
        }));

        for(EntityPlayer player : remove) {
            targets.remove(player);
        }
    }

    private List<EntityPlayer> getPlayersInRange(BlockPos origin, double range) {
        List<EntityPlayer> players = new ArrayList<>();
        AxisAlignedBB bb = new AxisAlignedBB(origin.getX() - range, origin.getY() - range, origin.getZ() - range,
                origin.getX() + range, origin.getY() + range, origin.getZ() + range);

        for(Entity e : getWorld().getEntitiesWithinAABBExcludingEntity(getPlayer(), bb)) {
            if(e instanceof EntityPlayer && e.getDistanceSq(origin) <= range * range) players.add((EntityPlayer) e);
        }

        return players;
    }

    private void playerKilled(EntityPlayer player) {
        MinecraftForge.EVENT_BUS.post(new PlayerKilledEvent(player));
    }
}
