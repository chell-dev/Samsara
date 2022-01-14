package me.chell.samsara.impl.module.render

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.module.Module
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.event.FovEvent
import me.chell.samsara.impl.event.MouseInputEvent

class Zoom: Module("Zoom", Category.RENDER)  {

    @Register val multi = Value("Multiplier", 0.16, "Lower value = more zoom.", sliderMin = 0.01, sliderMax = 1.0)
    @Register val scroll = Value("Scroll", true, "Change zoom amount with mouse wheel.")
    @Register val mouse = Value("Change Sensitivity", true, "Changes mouse sensitivity while zooming.")

    private var oldSens: Double? = null

    companion object {
        var multiplier: Double = 1.0
    }

    @EventHandler
    fun setFov(event: FovEvent) {
        if(multiplier < 0.01) multiplier = 0.01
        if(multiplier > 1.0) multiplier = 1.0
        var f = event.fov * multiplier
        if(f < 1.0) f = 1.0
        if(f > 179.0) f = 179.0
        event.fov = f

        if(oldSens == null)
            oldSens = mc.options.mouseSensitivity
        if(mouse.value)
            mc.options.mouseSensitivity = oldSens!! * multiplier
    }

    @EventHandler
    fun onMouseScroll(event: MouseInputEvent.Scroll) {
        if(scroll.value) {
            multiplier -= event.scrollAmount * 0.01
            event.cancel()
        }
    }

    override fun onDisable() {
        super.onDisable()
        multiplier = 1.0
        if(oldSens != null) {
            mc.options.mouseSensitivity = oldSens!!
            oldSens = null
        }
    }

    override fun onEnable() {
        super.onEnable()
        multiplier = multi.value
    }
}