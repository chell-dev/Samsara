package me.chell.samsara.impl.mixin;

import me.chell.samsara.Samsara;
import me.chell.samsara.api.event.EventManager;
import me.chell.samsara.impl.event.KeyInputEvent;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboard {
    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if(Samsara.INSTANCE.getLoaded())
            EventManager.INSTANCE.post(new KeyInputEvent(key, action, modifiers));
    }
}
