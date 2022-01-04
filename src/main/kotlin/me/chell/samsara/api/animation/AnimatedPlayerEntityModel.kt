package me.chell.samsara.api.animation

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.entity.LivingEntity

class AnimatedPlayerEntityModel<T: LivingEntity>(thinArms: Boolean): PlayerEntityModel<T>(MinecraftClient.getInstance().entityModelLoader.getModelPart(EntityModelLayers.PLAYER), thinArms) {
    override fun setAngles(livingEntity: T, f: Float, g: Float, h: Float, i: Float, j: Float) {}

    override fun animateModel(livingEntity: T, f: Float, g: Float, h: Float) {}
}