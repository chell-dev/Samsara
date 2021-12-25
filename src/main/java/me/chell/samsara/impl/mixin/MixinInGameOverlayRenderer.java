package me.chell.samsara.impl.mixin;

import me.chell.samsara.api.event.EventManager;
import me.chell.samsara.impl.event.RenderOverlayEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class MixinInGameOverlayRenderer {

    @Inject(method = "renderInWallOverlay", at = @At("HEAD"), cancellable = true)
    private static void wallOverlay(Sprite sprite, MatrixStack matrices, CallbackInfo ci) {
        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Type.WALL);
        EventManager.INSTANCE.post(event);
        if(event.getCanceled()) ci.cancel();
    }

    @Inject(method = "renderUnderwaterOverlay", at = @At("HEAD"), cancellable = true)
    private static void waterOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Type.WATER);
        EventManager.INSTANCE.post(event);
        if(event.getCanceled()) ci.cancel();
    }

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void fireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Type.FIRE);
        EventManager.INSTANCE.post(event);
        if(event.getCanceled()) ci.cancel();
    }
}
