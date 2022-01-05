package me.chell.samsara.impl.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.chell.samsara.api.event.EventManager;
import me.chell.samsara.impl.event.FovEvent;
import me.chell.samsara.impl.event.RenderEvent;
import me.chell.samsara.impl.event.RenderHandEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void zoom(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        FovEvent event = new FovEvent(cir.getReturnValue());
        EventManager.INSTANCE.post(event);
        cir.setReturnValue(event.getFov());
    }

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 1))
    private void renderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        MinecraftClient.getInstance().getProfiler().swap("samsara");
        RenderSystem.clear(256, MinecraftClient.IS_SYSTEM_MAC);
        EventManager.INSTANCE.post(new RenderEvent(matrices, tickDelta));
    }

    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
    private void renderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        EventManager.INSTANCE.post(new RenderHandEvent(matrices));
    }

}
