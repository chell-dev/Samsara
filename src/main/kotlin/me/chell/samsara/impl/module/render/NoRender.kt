package me.chell.samsara.impl.module.render

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.feature.Module
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.event.ParticleAddedEvent
import me.chell.samsara.impl.event.RenderOverlayEvent
import net.minecraft.client.particle.ExplosionLargeParticle
import net.minecraft.client.particle.ExplosionSmokeParticle
import net.minecraft.client.particle.SpellParticle

class NoRender: Module("NoRender", Category.RENDER) {

    @Register(0) val fire = Value("OnFire", true, "Overlay when you're on fire.")
    @Register(1) val water = Value("UnderWater", true, "Overlay when you're underwater.")
    @Register(2) val wall = Value("InWall", true, "Overlay when you're inside a block.")
    @Register(3) val explosions = Value("Explosions", true, "Explosion particles.")
    @Register(4) val potions = Value("Potion Effects", true, "Potion effect particles.")

    @EventHandler
    fun onRenderOverlay(event: RenderOverlayEvent) {
        when(event.type) {
            RenderOverlayEvent.Type.FIRE -> if(fire.value) event.cancel()
            RenderOverlayEvent.Type.WATER -> if(water.value) event.cancel()
            RenderOverlayEvent.Type.WALL -> if(wall.value) event.cancel()
        }
    }

    @EventHandler
    fun onAddParticle(event: ParticleAddedEvent) {
        if(explosions.value) {
            if(event.particle is ExplosionLargeParticle || event.particle is ExplosionSmokeParticle)
                event.cancel()
        }
        if(potions.value && event.particle is SpellParticle) {
            playerNullable ?: return
            val pos = event.particle.boundingBox.center
            if(player.eyePos.squaredDistanceTo(pos.x, pos.y, pos.z) <= 9.0)
                event.cancel()
        }
    }
}