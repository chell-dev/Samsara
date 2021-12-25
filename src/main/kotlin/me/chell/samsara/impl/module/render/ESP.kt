package me.chell.samsara.impl.module.render

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.module.Module
import me.chell.samsara.impl.event.RenderEvent
import net.minecraft.client.render.*
import net.minecraft.entity.Entity
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

class ESP: Module("ESP", Category.RENDER) {

    private val immediate = VertexConsumerProvider.immediate(BufferBuilder(256))

    @EventHandler
    fun onRender(event: RenderEvent) {
        if(mc.world == null) return

        for(e in world.players) {
            if(e == player) continue

            val vec3d = getPosition(e, event.tickDelta)

            event.matrices.push()
            event.matrices.translate(vec3d.x, vec3d.y, vec3d.z)
            val box = e.boundingBox.offset(-e.x, -e.y, -e.z)

            WorldRenderer.drawBox(event.matrices, immediate.getBuffer(RenderLayer.getLines()), box, 1f, 1f, 1f, .8f)

            event.matrices.pop()
        }

        immediate.draw()
    }

    private fun getPosition(entity: Entity, tickDelta: Float): Vec3d {
        val d = MathHelper.lerp(tickDelta.toDouble(), entity.lastRenderX, entity.x)
        val e = MathHelper.lerp(tickDelta.toDouble(), entity.lastRenderY, entity.y)
        val f = MathHelper.lerp(tickDelta.toDouble(), entity.lastRenderZ, entity.z)
        val camera = mc.gameRenderer.camera.pos
        return Vec3d(d - camera.x, e - camera.y, f - camera.z)
    }

}