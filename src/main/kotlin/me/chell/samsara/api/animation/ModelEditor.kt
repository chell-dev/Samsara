package me.chell.samsara.api.animation

import com.mojang.authlib.GameProfile
import me.chell.samsara.api.util.Globals
import me.chell.samsara.impl.mixin.render.AccessorLivingEntityRenderer
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.model.ModelPart
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.network.OtherClientPlayerEntity
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.text.LiteralText
import org.lwjgl.glfw.GLFW
import java.util.*

class ModelEditor: Screen(LiteralText("Model Editor")), Globals {

    private var selectedPart = "head"
    private var selectedValue = "pivotX"
    private val green = 0x00ff00
    var textY = 2f

    private val puppet: OtherClientPlayerEntity = OtherClientPlayerEntity(world, GameProfile(UUID.fromString(mc.session.uuid), "ModelEditorPuppet"))
    private val model: AnimatedPlayerEntityModel<out LivingEntity> = AnimatedPlayerEntityModel(player.model == "slim")

    private var add = 0f

    private val animation: Animation

    init {
        val rightArm = listOf(
            Keyframe(ModelTransform.of(-5.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.05f), 0),
            Keyframe(ModelTransform.of(-5.0f, 2.0f, 0.0f, 0.0f, 0.0f, 2.6f), 10),
            Keyframe(ModelTransform.of(-5.0f, 2.0f, 0.0f, 0.0f, 0.0f, 2f), 15),
            Keyframe(ModelTransform.of(-5.0f, 2.0f, 0.0f, 0.0f, 0.0f, 2.6f), 20),
            Keyframe(ModelTransform.of(-5.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.05f), 30),
        )

        val lefttArm = listOf(
            Keyframe(ModelTransform.of(5.0f, 2.0f, 0.0f, 0.0f, 0.0f, -0.05f), 0),
            Keyframe(ModelTransform.of(5.0f, 2.0f, 0.0f, 0.0f, 0.0f, -0.15f), 15),
            Keyframe(ModelTransform.of(5.0f, 2.0f, 0.0f, 0.0f, 0.0f, -0.05f), 30),
        )

        val r = Bone(model.rightArm, rightArm)
        val l = Bone(model.leftArm, lefttArm)

        animation = Animation(listOf(r, l))
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        when(keyCode) {
            GLFW.GLFW_KEY_1 -> selectedPart = "head"
            GLFW.GLFW_KEY_2 -> selectedPart = "body"
            GLFW.GLFW_KEY_3 -> selectedPart = "leftArm"
            GLFW.GLFW_KEY_4 -> selectedPart = "rightArm"
            GLFW.GLFW_KEY_5 -> selectedPart = "leftLeg"
            GLFW.GLFW_KEY_6 -> selectedPart = "rightLeg"

            GLFW.GLFW_KEY_Q -> selectedValue = "pivotX"
            GLFW.GLFW_KEY_W -> selectedValue = "pivotY"
            GLFW.GLFW_KEY_E -> selectedValue = "pivotZ"
            GLFW.GLFW_KEY_R -> selectedValue = "yaw"
            GLFW.GLFW_KEY_T -> selectedValue = "pitch"
            GLFW.GLFW_KEY_Y -> selectedValue = "roll"

            GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> player.sendMessage(LiteralText(transformToString()), false)

            GLFW.GLFW_KEY_UP -> add = 0.05f

            GLFW.GLFW_KEY_DOWN -> add = -0.05f

            else -> return super.keyPressed(keyCode, scanCode, modifiers)
        }
        return true
    }

    private fun getPart(): ModelPart = when(selectedPart) {
        "head" -> model.head
        "body" -> model.body
        "leftArm" -> model.leftArm
        "rightArm" -> model.rightArm
        "leftLeg" -> model.leftLeg
        else -> model.rightLeg
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if(keyCode == GLFW.GLFW_KEY_UP || keyCode == GLFW.GLFW_KEY_DOWN) {
            add = 0f
            return true
        }
        return false
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        textY = 2f

        drawString(matrices, "Keybinds:", -1)
        textY += 10f

        drawString(matrices, "1 = Select Head", if(selectedPart == "head") green else -1)
        drawString(matrices, "2 = Select Body", if(selectedPart == "body") green else -1)
        drawString(matrices, "3 = Select Left Arm", if(selectedPart == "leftArm") green else -1)
        drawString(matrices, "4 = Select Right Arm", if(selectedPart == "rightArm") green else -1)
        drawString(matrices, "5 = Select Left Leg", if(selectedPart == "leftLeg") green else -1)
        drawString(matrices, "6 = Select Right Leg", if(selectedPart == "rightLeg") green else -1)
        textY += 10f

        drawString(matrices, "Q = Select Pivot X", if(selectedValue == "pivotX") green else -1)
        drawString(matrices, "W = Select Pivot Y", if(selectedValue == "pivotY") green else -1)
        drawString(matrices, "E = Select Pivot Z", if(selectedValue == "pivotZ") green else -1)
        drawString(matrices, "R = Select Yaw", if(selectedValue == "yaw") green else -1)
        drawString(matrices, "T = Select Pitch", if(selectedValue == "pitch") green else -1)
        drawString(matrices, "Y = Select Roll", if(selectedValue == "roll") green else -1)
        textY += 10f

        drawString(matrices, "Up = Increase Value", -1)
        drawString(matrices, "Down = Decrease Value", -1)
        drawString(matrices, "Enter = Print Transforms", -1)

        val x = 475
        val y = 350
        val renderer = mc.entityRenderDispatcher.getRenderer(puppet) as LivingEntityRenderer<out LivingEntity, out EntityModel<out LivingEntity>>
        val m = renderer.model as BipedEntityModel<out LivingEntity>
        (renderer as AccessorLivingEntityRenderer).setModel(model)
        animation.render(mc.tickDelta)
        InventoryScreen.drawEntity(x, y, 100, x - mouseX.toFloat(), y - 150 - mouseY.toFloat(), puppet)
        (renderer as AccessorLivingEntityRenderer).setModel(m)
    }

    override fun tick() {
        if(add != 0f) {
            when(selectedValue) {
                "pivotX" -> getPart().pivotX += add
                "pivotY" -> getPart().pivotY += add
                "pivotZ" -> getPart().pivotZ += add
                "yaw" -> getPart().yaw += add
                "pitch" -> getPart().pitch += add
                "roll" -> getPart().roll += add
            }
        }

        animation.tick()
    }

    private fun drawString(matrices: MatrixStack?, text: String, color: Int) {
        fontRenderer.drawWithShadow(matrices, text, 2f, textY, color)
        textY += 10f
    }

    private fun transformToString(): String {
        val builder = StringBuilder()

        builder.append("Head: ")
        builder.append("Keyframe(ModelTransform.of(")
        builder.append(model.head.pivotX).append("f, ")
        builder.append(model.head.pivotY).append("f, ")
        builder.append(model.head.pivotZ).append("f, ")
        builder.append(model.head.pitch).append("f, ")
        builder.append(model.head.yaw).append("f, ")
        builder.append(model.head.roll).append("f), POS)")
        builder.append('\n')

        builder.append("Body: ")
        builder.append("Keyframe(ModelTransform.of(")
        builder.append(model.body.pivotX).append("f, ")
        builder.append(model.body.pivotY).append("f, ")
        builder.append(model.body.pivotZ).append("f, ")
        builder.append(model.body.pitch).append("f, ")
        builder.append(model.body.yaw).append("f, ")
        builder.append(model.body.roll).append("f), POS)")
        builder.append('\n')

        builder.append("Left Arm: ")
        builder.append("Keyframe(ModelTransform.of(")
        builder.append(model.leftArm.pivotX).append("f, ")
        builder.append(model.leftArm.pivotY).append("f, ")
        builder.append(model.leftArm.pivotZ).append("f, ")
        builder.append(model.leftArm.pitch).append("f, ")
        builder.append(model.leftArm.yaw).append("f, ")
        builder.append(model.leftArm.roll).append("f), POS)")
        builder.append('\n')

        builder.append("Right Arm: ")
        builder.append("Keyframe(ModelTransform.of(")
        builder.append(model.rightArm.pivotX).append("f, ")
        builder.append(model.rightArm.pivotY).append("f, ")
        builder.append(model.rightArm.pivotZ).append("f, ")
        builder.append(model.rightArm.pitch).append("f, ")
        builder.append(model.rightArm.yaw).append("f, ")
        builder.append(model.rightArm.roll).append("f), POS)")
        builder.append('\n')

        builder.append("Left Leg: ")
        builder.append("Keyframe(ModelTransform.of(")
        builder.append(model.leftLeg.pivotX).append("f, ")
        builder.append(model.leftLeg.pivotY).append("f, ")
        builder.append(model.leftLeg.pivotZ).append("f, ")
        builder.append(model.leftLeg.pitch).append("f, ")
        builder.append(model.leftLeg.yaw).append("f, ")
        builder.append(model.leftLeg.roll).append("f), POS)")
        builder.append('\n')

        builder.append("Right Leg: ")
        builder.append("Keyframe(ModelTransform.of(")
        builder.append(model.rightLeg.pivotX).append("f, ")
        builder.append(model.rightLeg.pivotY).append("f, ")
        builder.append(model.rightLeg.pivotZ).append("f, ")
        builder.append(model.rightLeg.pitch).append("f, ")
        builder.append(model.rightLeg.yaw).append("f, ")
        builder.append(model.rightLeg.roll).append("f), POS)")
        builder.append('\n')

        return builder.toString()
    }
}