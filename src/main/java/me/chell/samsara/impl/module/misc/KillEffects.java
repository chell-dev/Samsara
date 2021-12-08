package me.chell.samsara.impl.module.misc;

import com.google.common.collect.Lists;
import me.chell.samsara.Samsara;
import me.chell.samsara.api.event.PlayerKilledEvent;
import me.chell.samsara.api.event.PlayerUpdateEvent;
import me.chell.samsara.api.module.Module;
import me.chell.samsara.api.util.ChatUtil;
import me.chell.samsara.api.value.Value;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class KillEffects extends Module {

    public KillEffects() {
        super("KillEffects", Category.MISC);
        sounds = Lists.newArrayList(
                registerSound("godlike"),
                registerSound("holyshit"),
                registerSound("impressive"),
                registerSound("ownage"),
                registerSound("perfect"),
                registerSound("wickedsick")
        );
        playerMap = new HashMap<>();
        rand = new Random();
    }

    private final Value<QuakeMode> quake = builder("Announcer", QuakeMode.OFF).build(); // Doesn't work in dev environment because idk how to do the sounds.json for assets/minecraft
    private final Value<ChatMode> chat = builder("Chat", ChatMode.OFF).build();
    private final Value<String> message = builder("Message", "GG ${player}, well played!").visible(b -> chat.getValue() != ChatMode.OFF).build();
    private final Value<ParticleMode> particles = builder("Particles", ParticleMode.OFF).build();
    private final Value<Boolean> particleSound = builder("Sound", true).visible(b -> particles.getValue() == ParticleMode.THUNDER).build();
    private final Value<Float> heartsTime = builder("Time", 2f).bounds(0f, 5f).visible(b -> particles.getValue() == ParticleMode.HEARTS).build();
    private final Value<Integer> heartsAmount = builder("Amount", 8).bounds(1, 10).visible(b -> particles.getValue() == ParticleMode.HEARTS).build();

    private final List<SoundEvent> sounds;
    private int kills;
    private final HashMap<EntityPlayer, Integer> playerMap;
    private final Random rand;

    @SubscribeEvent
    public void onPlayerKilled(PlayerKilledEvent event) {
        kills++;
        sendMessage(event.getTarget());
        doParticleEffect(event.getTarget());
        playSound();
    }

    @SubscribeEvent
    public void onWorldLoaded(WorldEvent.Load event) {
        kills = 0;
        playerMap.clear();
    }

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        // net.minecraft.entity.passive.EntityAnimal
        List<EntityPlayer> remove = new ArrayList<>();

        for(EntityPlayer player : playerMap.keySet()) {
            int particleTime = playerMap.get(player);
            if(particleTime > 0) {
                playerMap.put(player, particleTime-1);

                if (particleTime % (11-heartsAmount.getValue()) == 0) {
                    double d0 = rand.nextGaussian() * 0.02D;
                    double d1 = rand.nextGaussian() * 0.02D;
                    double d2 = rand.nextGaussian() * 0.02D;
                    getWorld().spawnParticle(EnumParticleTypes.HEART,
                            player.posX + (double)(rand.nextFloat() * player.width * 4.0F) - (double)player.width,
                            player.posY + 0.5D + (double)(rand.nextFloat() * player.height),
                            player.posZ + (double)(rand.nextFloat() * player.width * 4.0F) - (double)player.width,
                            d0, d1, d2);
                }
            } else {
                remove.add(player);
            }
        }

        for(EntityPlayer player : remove) {
            playerMap.remove(player);
        }
    }

    private void sendMessage(EntityPlayer target) {
        if(chat.getValue().equals(ChatMode.CLIENT)) {
            ChatUtil.sendClientMessage(message.getValue(), ChatUtil.var("player", target.getName()));
        } else if(chat.getValue().equals(ChatMode.ON)) {
            ChatUtil.sendChatMessage(message.getValue(), ChatUtil.var("player", target.getName()));
        }
    }

    private void playSound() {
        if(quake.getValue().equals(QuakeMode.RANDOM)) {
            int rand = ThreadLocalRandom.current().nextInt(0, sounds.size());
            getPlayer().playSound(sounds.get(rand), 1f, 1f);
        }
    }

    private void doParticleEffect(EntityPlayer target) {
        switch (particles.getValue()) {
            case THUNDER:
                getWorld().addWeatherEffect(new EntityLightningBolt(getWorld(), target.posX, target.posY, target.posZ, true));
                if(particleSound.getValue()) {
                    getWorld().playSound(getPlayer(), target.posX, target.posY, target.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.9F);
                    getWorld().playSound(getPlayer(), target.posX, target.posY, target.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 2.0F, 0.6F);
                }
                break;
            case HEARTS:
                playerMap.put(target, (int)(heartsTime.getValue() * 20));
                break;
        }
    }

    private SoundEvent registerSound(String name){
        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(Samsara.MODID, name));
        soundEvent.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(soundEvent);
        return soundEvent;
    }

    private enum QuakeMode {
        OFF, RANDOM, MULTI // TODO double kill, triple kill etc. I can't find the proper sound effects anywhere
    }

    private enum ChatMode {
        OFF, ON, CLIENT
    }

    private enum ParticleMode {
        OFF, THUNDER, HEARTS
    }
}
