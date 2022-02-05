package me.chell.samsara.impl.mixin.render;

import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HeldItemRenderer.class)
public interface AccessorHeldItemRenderer {
    @Accessor("equipProgressMainHand") void setEquipProgressMainHand(float value);
    @Accessor("equipProgressOffHand") void setEquipProgressOffHand(float value);
    @Accessor("mainHand") void setMainHand(ItemStack value);
    @Accessor("offHand") void setOffHand(ItemStack value);
}
