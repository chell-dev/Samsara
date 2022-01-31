package me.chell.samsara.impl.widget

import com.mojang.blaze3d.systems.RenderSystem
import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.feature.Widget
import me.chell.samsara.impl.event.KeyInputEvent
import me.chell.samsara.impl.event.PlayerTickEvent
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class Neko: Widget("Neko") {

    private val idle = Identifier(MODID, "textures/neko/neko_idle.png")
    private val asleep = Identifier(MODID, "textures/neko/neko_asleep.png")
    private val alert = Identifier(MODID, "textures/neko/neko_alert.png")

    init {
        width = 32.0
        height = 32.0
    }

    private var inputTicks = 0
    private var ticks = 0
    private var frame = 0

    private var state = State.IDLE

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        super.render(matrices, mouseX, mouseY, tickDelta)
        if(mouseX < 0 && !pinned.value) return

        when(state) {
            State.IDLE -> {
                RenderSystem.setShaderTexture(0, idle)
                DrawableHelper.drawTexture(matrices, x.value.toInt(), y.value.toInt(), 0f, frame*32f, 32, 32, 32, 160)
            }
            State.ASLEEP -> {
                RenderSystem.setShaderTexture(0, asleep)
                DrawableHelper.drawTexture(matrices, x.value.toInt(), y.value.toInt(), 0f, frame*32f, 32, 32, 32, 128)
            }
            State.ALERT -> {
                RenderSystem.setShaderTexture(0, alert)
                DrawableHelper.drawTexture(matrices, x.value.toInt(), y.value.toInt(), 0f, 0f, 32, 32, 32, 32)
            }
        }
    }

    @EventHandler
    fun onTick(event: PlayerTickEvent) {
        when(state) {
            State.IDLE -> {
                inputTicks++
                if(inputTicks > 600) {
                    state = State.ASLEEP
                    ticks = 1
                    frame = 0
                }

                if(frame*32 >= 160) {
                    ticks = 1
                    frame = 0
                } else ticks++

                val duration = if(frame == 0) 200 else 4
                if(ticks % duration == 0) frame++
            }

            State.ASLEEP -> {
                if(frame*32 >= 128) {
                    ticks = 1
                    frame = 0
                } else ticks++

                if(ticks % 20 == 0) frame++
            }

            State.ALERT -> {
                if(ticks >= 40) {
                    ticks = 0
                    frame = 0
                    state = State.IDLE
                } else ticks++
            }
        }
    }

    @EventHandler
    fun onInput(event: KeyInputEvent) {
        inputTicks = 0
        if(state == State.ASLEEP) {
            state = State.ALERT
            ticks = 1
            frame = 0
        }
    }

    enum class State {
        IDLE, ASLEEP, ALERT
    }
}