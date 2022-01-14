package me.chell.samsara.impl.module.combat

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.module.Module
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.event.EntityKilledEvent
import me.chell.samsara.impl.event.PlayerTickEvent
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundEvent
import net.minecraft.text.LiteralText
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import kotlin.random.Random

class KillEffects: Module("KillEffects", Category.COMBAT) {

    private val sounds = mutableListOf<SoundEvent>()

    init {
        sounds.add(registerSoundEvent("godlike"))
        sounds.add(registerSoundEvent("holyshit"))
        sounds.add(registerSoundEvent("impressive"))
        sounds.add(registerSoundEvent("ownage"))
        sounds.add(registerSoundEvent("perfect"))
        sounds.add(registerSoundEvent("wickedsick"))
    }

    @Register(0) val quake = Value("Announcer", true)

    @Register(1) val chat = Value("Chat", Chat.CLIENT)
    @Register(2) val message = Value("Message", "GG, \${target}, well played!", visible = { chat.value != Chat.OFF })

    @Register(3) val particles = Value("Particles", Particles.OFF)
    @Register(4) val amount = Value("Amount", 8, min = 1, max = 10, visible = { particles.value != Particles.OFF })
    @Register(5) val time = Value("Time", 2.0, min = 0.0, max = 5.0, visible = { particles.value == Particles.HEARTS })

    @Register(6) val players = Value("Players", true)
    @Register(7) val hostile = Value("Hostile", false)
    @Register(8) val passive = Value("Passive", false)

    private val entityMap = mutableMapOf<LivingEntity, Int>()

    @EventHandler
    fun onKill(event: EntityKilledEvent) {
        sendMessage(event.target)
        spawnParticles(event.target)
        playSound()
    }

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        val list = mutableListOf<LivingEntity>()

        for(entity in entityMap.keys.toList()) {
            val t = entityMap[entity]!!
            if(t > 0) {
                entityMap[entity] = t-1

                if(t % (11-amount.value) == 0) {
                    for(i in 0..2) {
                        val d0 = Random.nextDouble() * 0.02
                        val d1 = Random.nextDouble() * 0.02
                        val d2 = Random.nextDouble() * 0.02

                        world.addParticle(
                            ParticleTypes.HEART,
                            entity.x + Random.nextDouble() * entity.width * 4.0 - entity.width,
                            entity.y + 0.5 + Random.nextDouble() * entity.height,
                            entity.z + Random.nextDouble() * entity.width * 4.0 - entity.width,
                            d0, d1, d2
                        )
                    }
                }
            } else {
                list.add(entity)
            }
        }

        for(entity in list) {
            entityMap.remove(entity)
        }
    }

    private fun sendMessage(target: LivingEntity) { // TODO ChatManager to avoid spam
        val msg = message.value.replace("\${target}", target.name.string)
        when(chat.value) {
            Chat.SERVER -> {
                player.sendChatMessage(msg)
            }
            Chat.CLIENT -> {
                player.sendMessage(LiteralText(msg), false)
            }
            else -> {}
        }
    }

    private fun playSound() {
        if(quake.value)
            player.playSound(sounds[Random.nextInt(sounds.size)], 1f, 1f)
    }

    private fun spawnParticles(target: LivingEntity) {
        when(particles.value) {
            Particles.LIGHTNING -> {
                for(i in 0 until amount.value) {
                    val entity = EntityType.LIGHTNING_BOLT.create(world)!!
                    entity.setCosmetic(true)
                    entity.refreshPositionAfterTeleport(target.pos)
                    world.addEntity(6342792+i, entity) // random number hopefully high enough to not delete any entities
                }

            }
            Particles.HEARTS -> {
                entityMap[target] = (time.value * 20).toInt()
            }
            else -> {}
        }
    }

    private fun registerSoundEvent(name: String): SoundEvent {
        val id = Identifier("$MODID:$name")
        val event = SoundEvent(id)
        Registry.register(Registry.SOUND_EVENT, id, event)
        return event
    }

    private fun isValid(entity: LivingEntity): Boolean {
        return (entity is PlayerEntity && players.value)
                || (entity is HostileEntity && hostile.value)
                || (entity is PassiveEntity && passive.value)
    }

    enum class Chat {
        OFF, SERVER, CLIENT
    }

    enum class Particles {
        OFF, LIGHTNING, HEARTS
    }

}