package me.chell.samsara.impl.mixin.entity;

import me.chell.samsara.api.event.EventManager;
import me.chell.samsara.impl.event.AttackEntityEvent;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {

    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void preAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        EventManager.INSTANCE.post(new AttackEntityEvent.Pre(target));
    }

    @Inject(method = "attackEntity", at = @At("TAIL"))
    private void postAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        EventManager.INSTANCE.post(new AttackEntityEvent.Post(target));
    }

}
