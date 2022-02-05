package me.chell.samsara.impl.mixin.client;

import me.chell.samsara.api.event.EventManager;
import me.chell.samsara.impl.event.MouseInputEvent;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MixinMouse {

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        MouseInputEvent.Scroll event = new MouseInputEvent.Scroll(vertical);
        EventManager.INSTANCE.post(event);
        if(event.getCanceled()) ci.cancel();
    }

    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    private void onButton(long window, int button, int action, int mods, CallbackInfo ci) {
        MouseInputEvent.Button event = new MouseInputEvent.Button(button, action, mods);
        EventManager.INSTANCE.post(event);
        if(event.getCanceled()) ci.cancel();
    }

}
