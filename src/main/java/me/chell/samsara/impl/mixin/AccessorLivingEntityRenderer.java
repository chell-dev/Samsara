package me.chell.samsara.impl.mixin;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntityRenderer.class)
public interface AccessorLivingEntityRenderer {
    @Accessor("model")
    void setModel(EntityModel<?> model);
}
