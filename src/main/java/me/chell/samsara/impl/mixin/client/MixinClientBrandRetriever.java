package me.chell.samsara.impl.mixin.client;

import me.chell.samsara.api.feature.FeatureManager;
import me.chell.samsara.impl.module.misc.FakeVanilla;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientBrandRetriever.class)
public class MixinClientBrandRetriever {

    @Inject(method = "getClientModName", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getBrand(CallbackInfoReturnable<String> cir) {
        FakeVanilla module = FeatureManager.INSTANCE.getModule("FakeVanilla");
        if(module != null && module.isEnabled()) {
            cir.cancel();
            cir.setReturnValue("vanilla");
        }
    }

}
