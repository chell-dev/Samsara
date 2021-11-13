package me.chell.samsara.impl.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.chell.samsara.api.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;

public class TestModule extends Module {
    public TestModule() {
        super("Test", Category.MISC);
        setBind(Keyboard.KEY_P);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Minecraft.getMinecraft().ingameGUI.getChatGUI()
                .printChatMessageWithOptionalDeletion(new TextComponentString(ChatFormatting.GREEN+"Enabled"), 0);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.getMinecraft().ingameGUI.getChatGUI()
                .printChatMessageWithOptionalDeletion(new TextComponentString(ChatFormatting.RED+"Disabled"), 0);
    }
}
