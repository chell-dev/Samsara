package me.chell.samsara.impl.mixin.entity;

import me.chell.samsara.api.event.EventManager;
import me.chell.samsara.impl.event.ChatEvent;
import me.chell.samsara.impl.event.PlayerTickEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    @Shadow @Final public ClientPlayNetworkHandler networkHandler;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        EventManager.INSTANCE.post(new PlayerTickEvent());
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void sendMessage(String message, CallbackInfo ci) {
        ChatEvent event = new ChatEvent(message);
        EventManager.INSTANCE.post(event);
        ci.cancel();
        if(!event.getCanceled()) {
            networkHandler.sendPacket(new ChatMessageC2SPacket(event.getMessage()));
        }
    }
}