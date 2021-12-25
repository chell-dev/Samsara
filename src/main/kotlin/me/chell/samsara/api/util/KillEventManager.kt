package me.chell.samsara.api.util

import me.chell.samsara.api.Loadable
import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.event.EventManager
import me.chell.samsara.impl.event.AttackEntityEvent
import me.chell.samsara.impl.event.EntityKilledEvent
import me.chell.samsara.impl.event.PlayerTickEvent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.decoration.EndCrystalEntity
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d

object KillEventManager: Loadable, Globals {

    private val targets = mutableMapOf<LivingEntity, Int>()

    private const val timeout = 60 // 3 seconds

    override fun load() {
        EventManager.register(this)
    }

    override fun unload() {
        EventManager.unregister(this)
        targets.clear()
    }

    @EventHandler
    fun onAttackEntity(event:AttackEntityEvent.Post) {
        if(event.target is LivingEntity) {
            targets[event.target] = timeout
        }
        else if(event.target is EndCrystalEntity) {
            addEntitiesInRange(event.target.pos, 10.0)
        }
    }

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        val list = mutableListOf<LivingEntity>()

        for(entity in targets.keys) {
            val ticks = targets[entity]!!

            if(ticks <= 0) {
                list.add(entity)
                continue
            }

            if(entity.isDead || entity.health <= 0.0) {
                list.add(entity)
                EventManager.post(EntityKilledEvent(entity))
                continue
            }

            targets[entity] = ticks-1
        }

        for(entity in list) {
            targets.remove(entity)
        }
    }

    private fun addEntitiesInRange(pos: Vec3d, range: Double) {
        val box = Box(
            pos.x - range, pos.y - range, pos.z - range,
            pos.x + range, pos.y + range, pos.z + range,
        )

        for(entity in world.getOtherEntities(player, box)) {
            if(entity is LivingEntity && pos.distanceTo(entity.pos) <= range)
                targets[entity] = timeout
        }
    }
}