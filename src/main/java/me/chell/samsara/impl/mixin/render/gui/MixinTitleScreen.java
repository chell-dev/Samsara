package me.chell.samsara.impl.mixin.render.gui;

import me.chell.samsara.Samsara;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {

    @Inject(method = "<init>(Z)V", at = @At("TAIL"))
    private void init(boolean doBackgroundFade, CallbackInfo ci) {
        Samsara.INSTANCE.updateMOTD();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, Samsara.INSTANCE.getMOTDFormatted(), 5, 5, -1);
    }
}
