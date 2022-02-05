package me.chell.samsara.impl.mixin.render.gui;

import me.chell.samsara.api.event.EventManager;
import me.chell.samsara.impl.event.RenderHudEvent;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Shadow @Final private static Identifier PUMPKIN_BLUR;

    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    private void preRenderOverlay(Identifier texture, float opacity, CallbackInfo ci) {
        if(texture != PUMPKIN_BLUR) return;
        RenderHudEvent.Pumpkin event = new RenderHudEvent.Pumpkin();
        EventManager.INSTANCE.post(event);
        if(event.getCanceled()) ci.cancel();
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void preRenderPortal(float nauseaStrength, CallbackInfo ci) {
        RenderHudEvent.Portal event = new RenderHudEvent.Portal();
        EventManager.INSTANCE.post(event);
        if(event.getCanceled()) ci.cancel();
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void preRenderCrosshair(MatrixStack matrices, CallbackInfo ci) {
        RenderHudEvent.Crosshair event = new RenderHudEvent.Crosshair(matrices);
        EventManager.INSTANCE.post(event);
        if(event.getCanceled()) ci.cancel();
    }

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    private void preRenderStatusEffectOverlay(MatrixStack matrices, CallbackInfo ci) {
        RenderHudEvent.StatusEffect event = new RenderHudEvent.StatusEffect(matrices);
        EventManager.INSTANCE.post(event);
        if(event.getCanceled()) ci.cancel();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void postRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        RenderHudEvent.Post event = new RenderHudEvent.Post(matrices, tickDelta);
        EventManager.INSTANCE.post(event);
    }
}
