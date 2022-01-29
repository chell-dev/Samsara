package me.chell.samsara.impl.mixin;

import me.chell.samsara.api.feature.FeatureManager;
import me.chell.samsara.impl.module.render.BetterPingDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {

    @Shadow @Final private MinecraftClient client;

    private BetterPingDisplay module;

    @Inject(method = "renderLatencyIcon", at = @At("HEAD"), cancellable = true)
    private void pong(MatrixStack matrices, int width, int x, int y, PlayerListEntry entry, CallbackInfo ci) {
        if(module.isEnabled()) {
            ci.cancel();
            client.textRenderer.drawWithShadow(matrices, entry.getLatency()+"ms", x + width - client.textRenderer.getWidth(entry.getLatency()+"ms"), y, 0x00ff00);
        }
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = 13))
    public int changeWidth(int constant) {
        if(module == null) {
            module = FeatureManager.INSTANCE.getModule("BetterPingDisplay");
            return constant;
        }
        return module.isEnabled() ? 41 : constant;
    }
}
