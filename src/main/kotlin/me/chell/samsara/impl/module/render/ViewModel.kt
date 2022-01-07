package me.chell.samsara.impl.module.render

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.module.Module
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.event.PlayerTickEvent
import me.chell.samsara.impl.event.RenderHandEvent
import me.chell.samsara.impl.mixin.AccessorHeldItemRenderer
import net.minecraft.util.math.Quaternion

class ViewModel: Module("ViewModel", Category.RENDER) {
    @Register(0) val setOffhandProgress = Value("Offhand Progress", false)
    @Register(1) val offhandValue = Value("Offhand Value", 0.5f, sliderMin = 0f, sliderMax = 1f, displayName = "Value", visible = {setOffhandProgress.value})
    @Register(2) val setMainhandProgress = Value("Mainhand Progress", false)
    @Register(3) val mainhandValue = Value("Mainhand Value", 1f, sliderMin = 0f, sliderMax = 1f, displayName = "Value", visible = {setMainhandProgress.value})
    @Register(4) val fov = Value("FOV", 70, sliderMin = 40, sliderMax = 150)
    @Register(5) val yOffset = Value("Offset Y", 0.0, sliderMin = -1.0, sliderMax = 1.0)
    @Register(5) val zOffset = Value("Offset Z", 0.0, sliderMin = -1.0, sliderMax = 1.0)
    @Register(6) val xRotation = Value("Rotation", 0, sliderMin = -90, sliderMax = 90)

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        val renderer = mc.gameRenderer.firstPersonRenderer as AccessorHeldItemRenderer
        if(setMainhandProgress.value) {
            renderer.setEquipProgressMainHand(mainhandValue.value)
            renderer.setMainHand(player.mainHandStack)
        }

        if(setOffhandProgress.value) {
            renderer.setEquipProgressOffHand(offhandValue.value)
            renderer.setOffHand(player.offHandStack)
        }
    }

    @EventHandler
    fun onRenderHand(event: RenderHandEvent) {
        mc.gameRenderer.loadProjectionMatrix(mc.gameRenderer.getBasicProjectionMatrix(fov.value.toDouble() * Zoom.multiplier))
        event.matrices.translate(0.0, yOffset.value, zOffset.value)
        event.matrices.multiply(Quaternion(xRotation.value.toFloat(), 0f, 0f, true))
    }
}