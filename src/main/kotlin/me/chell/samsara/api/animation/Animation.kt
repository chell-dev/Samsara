package me.chell.samsara.api.animation

import net.minecraft.client.model.ModelPart
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.MathHelper

data class Keyframe(val transform: ModelTransform, val position: Int /* where this keyframe is on the timeline */)

data class Bone(val modelPart: ModelPart, val keyframes: List<Keyframe>)

class Animation(val bones: List<Bone>) {

    fun testAnimation() {
        val playerModel: PlayerEntityModel<out LivingEntity>? = null
        playerModel!!

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

        val r = Bone(playerModel.rightArm, rightArm)
        val l = Bone(playerModel.leftArm, lefttArm)

        val anim = Animation(listOf(r, l))
    }

    var currentTick = 0

    val nextKeyframes = mutableMapOf<Bone, Int>()

    init {
        for(bone in bones) {
            nextKeyframes[bone] = 1
        }
    }

    fun tick() {
        currentTick++
    }

    fun render(tickDelta: Float) {
        for(bone in bones) {
            val n = nextKeyframes[bone]!!

            val previous = bone.keyframes[n - 1]
            val next = bone.keyframes[n]

            val diff = next.position - previous.position
            if(diff == 0) throw IllegalStateException("Two keyframes at the same position")
            val passed = currentTick - previous.position

            val time = ((passed + tickDelta) / diff).coerceAtMost(1f)

            val pivot = doPivot(previous, next, bone, time)
            val rotation = doRotation(previous, next, bone, time)

            if(pivot && rotation) {
                if(n == bone.keyframes.size - 1) {
                    nextKeyframes[bone] = 1
                    currentTick = 0
                } else
                    nextKeyframes[bone] = n + 1
            }
        }
    }

    private fun doPivot(previous: Keyframe, next: Keyframe, bone: Bone, time: Float): Boolean {
        var same = true

        if(bone.modelPart.pivotX != next.transform.pivotX) {
            bone.modelPart.pivotX = lerp(previous.transform.pivotX, next.transform.pivotX, time)
            same = false
        }
        if(bone.modelPart.pivotY != next.transform.pivotY) {
            bone.modelPart.pivotY = lerp(previous.transform.pivotY, next.transform.pivotY, time)
            same = false
        }
        if(bone.modelPart.pivotZ != next.transform.pivotZ) {
            bone.modelPart.pivotZ = lerp(previous.transform.pivotZ, next.transform.pivotZ, time)
            same = false
        }

        return same
    }

    private fun doRotation(previous: Keyframe, next: Keyframe, bone: Bone, time: Float): Boolean {
        var same = true

        if(bone.modelPart.pitch != next.transform.pitch) {
            bone.modelPart.pitch = lerp(previous.transform.pitch, next.transform.pitch, time)
            same = false
        }
        if(bone.modelPart.yaw != next.transform.yaw) {
            bone.modelPart.yaw = lerp(previous.transform.yaw, next.transform.yaw, time)
            same = false
        }
        if(bone.modelPart.roll != next.transform.roll) {
            bone.modelPart.roll = lerp(previous.transform.roll, next.transform.roll, time)
            same = false
        }

        return same
    }

    /**
     * @param a = point A
     * @param b = point B
     * @param t = elapsed time (0.0 .. 1.0)
     */
    private fun lerp(a: Float, b: Float, t: Float): Float = a * (1 - t) + b * t
}