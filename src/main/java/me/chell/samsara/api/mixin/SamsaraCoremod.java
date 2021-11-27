package me.chell.samsara.api.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

public class SamsaraCoremod implements IFMLLoadingPlugin {
        public static boolean isObfuscatedEnvironment = false;

        public SamsaraCoremod() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.samsara.json");
        }

        @Override
        public String[] getASMTransformerClass() {
                return new String[0];
        }

        @Override
        public String getModContainerClass() {
                return null;
        }

        @Override
        public String getSetupClass() {
                return null;
        }

        @Override
        public void injectData(Map<String, Object> data) {
        }

        @Override
        public String getAccessTransformerClass() {
                return null;
        }
}
