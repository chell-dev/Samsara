package me.chell.samsara.api.mixin;

import me.chell.samsara.api.event.PlayerUpdateEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class EntityPlayerSPMixin {
    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void onUpdate(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PlayerUpdateEvent());
    }
}