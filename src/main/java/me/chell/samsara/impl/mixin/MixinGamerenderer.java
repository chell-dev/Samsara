package me.chell.samsara.impl.mixin;

import me.chell.samsara.api.event.EventManager;
import me.chell.samsara.impl.event.FovEvent;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class MixinGamerenderer {

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void zoom(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        FovEvent event = new FovEvent(cir.getReturnValue());
        EventManager.INSTANCE.post(event);
        cir.setReturnValue(event.getFov());
    }

}
