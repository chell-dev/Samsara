package me.chell.samsara.impl.module.combat

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.module.Module
import me.chell.samsara.api.module.ModuleManager
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.api.value.ValueBuilder
import me.chell.samsara.impl.event.PlayerTickEvent
import me.chell.samsara.impl.module.movement.PullDown
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.AxeItem
import net.minecraft.item.SwordItem
import net.minecraft.util.Hand

class KillAura: Module("KillAura", Category.COMBAT, "Attack other entities.") {

    @Register val range = ValueBuilder("Range", 5.5).bounds(0.0, 7.0).build()
    @Register val players = Value("Players", true)
    @Register val hostile = Value("Hostile", false)
    @Register val passive = Value("Passive", false)
    @Register val delay = Value("1.9 Delay", true)
    @Register val sword = Value("Sword Only", false, "Only attack when holding a sword.")
    @Register val axe = ValueBuilder("Use Axe", false).visible{sword.value}.description("Also attack when holding an axe.").build()
    @Register val crits = Value("Criticals", CritMode.Off)

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        if(sword.value) {
            val item = player.mainHandStack.item
            if (item !is SwordItem && !(axe.value && item is AxeItem)) {
                return
            }
        }

        val target = world.entities
            .asSequence()
            .filter { it != player }
            .filterIsInstance<LivingEntity>()
            .filter { (it is PlayerEntity && players.value)
                    || (it is HostileEntity && hostile.value)
                    || (it is PassiveEntity && passive.value)
            }
            .filter { player.isInRange(it, range.value) }
            .filter { it.isAlive && it.health > 0 }
            .filter { it.hurtTime == 0 }
            .sortedBy { player.distanceTo(it) }
            .firstOrNull() ?: return

        if(!delay.value || player.getAttackCooldownProgress(0F) >= 1) {
            if(crits.value == CritMode.Jump) {
                if(player.isOnGround) player.jump()
                if(player.fallDistance <= 0) return
            }
            if(crits.value == CritMode.MiniJump) {
                if(player.isOnGround) {
                    player.addVelocity(0.0, if(ModuleManager.getModule<PullDown>("PullDown")!!.isEnabled()) 1.12 else 0.12, 0.0)
                }
                if(player.fallDistance <= 0) return
            }

            interactionManager.attackEntity(player, target)
            player.swingHand(Hand.MAIN_HAND)
        }
    }

    enum class CritMode {
        Off, MiniJump, Jump
    }
}