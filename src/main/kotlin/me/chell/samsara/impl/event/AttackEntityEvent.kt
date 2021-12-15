package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event
import net.minecraft.entity.Entity

abstract class AttackEntityEvent(val target: Entity): Event() {
    class Pre(target: Entity): AttackEntityEvent(target)
    class Post(target: Entity): AttackEntityEvent(target)
}