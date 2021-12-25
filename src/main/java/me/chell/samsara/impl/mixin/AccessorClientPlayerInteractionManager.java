package me.chell.samsara.impl.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerInteractionManager.class)
public interface AccessorClientPlayerInteractionManager {
    @Accessor("blockBreakingCooldown")
    void setBlockBreakingCooldown(int cooldown);
}
