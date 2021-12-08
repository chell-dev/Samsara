package me.chell.samsara.api.util;

import me.chell.samsara.Samsara;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class ChatUtil implements Wrapper {

    private static ChatUtil INSTANCE;
    private final List<Variable> variables;

    public ChatUtil() {
        INSTANCE = this;
        variables = new ArrayList<>();
        variables.add(new Variable("client_name", Samsara.NAME));
        variables.add(new Variable("client_version", Samsara.VERSION));
    }

    public static void sendClientMessage(String text, Variable... vars) {
        INSTANCE.clientMessage(text, vars);
    }

    public static void sendChatMessage(String text, Variable... vars) {
        INSTANCE.chatMessage(text, vars);
    }

    public static String replaceVars(String input, Variable... vars) {
        return INSTANCE.rVars(input, vars);
    }

    private void clientMessage(String text, Variable... vars) {
        text = rVars(text, vars);
        getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(
                ClientValues.msgPrefixColor.getValue() + rVars("[${client_name}] ")
                        + ClientValues.msgColor.getValue() + text), ClientValues.msgDelete.getValue() ? 696 : 0);
    }

    private void chatMessage(String text, Variable... vars) {
        text = rVars(text, vars);
        getPlayer().sendChatMessage(text);
    }

    private String rVars(String input, Variable... vars) {
        for(Variable v : variables) {
            input = input.replace(v.name, v.value);
        }
        for(Variable v : vars) {
            input = input.replace(v.name, v.value);
        }
        return input;
    }

    public static Variable var(String name, String value) {
        return new Variable(name, value);
    }

    public static class Variable {
        public String name;
        public String value;

        public Variable(String name, String value) {
            this.name = "${"+name+"}";
            this.value = value;
        }
    }
}
