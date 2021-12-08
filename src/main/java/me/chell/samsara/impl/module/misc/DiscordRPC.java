package me.chell.samsara.impl.module.misc;

import me.chell.samsara.api.DiscordActivity;
import me.chell.samsara.api.event.PlayerUpdateEvent;
import me.chell.samsara.api.module.Module;
import me.chell.samsara.api.util.ChatUtil;
import me.chell.samsara.api.value.Value;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.List;

public class DiscordRPC extends Module {
    public DiscordRPC() {
        super("DiscordRPC", Category.MISC);
    }

    private final ChatUtil.Variable playerVar = ChatUtil.var("player_name", getMinecraft().getSession().getUsername());
    private final ChatUtil.Variable serverName = ChatUtil.var("server_name", "null");
    private final ChatUtil.Variable serverIP = ChatUtil.var("server_ip", "null");
    private final ChatUtil.Variable serverPing = ChatUtil.var("server_ping", "0ms");
    private final ChatUtil.Variable dimensionVar = ChatUtil.var("dimension", "null");

    private final DiscordValue menu = new DiscordValue("Menu text", "Main Menu", getValues(), null, playerVar);
    private final DiscordValue details = new DiscordValue("Details", "Version ${client_version}", getValues(), menu, playerVar, serverName, serverIP, serverPing, dimensionVar);
    private final DiscordValue state = new DiscordValue("State", "Playing ${server_name}", getValues(), menu, playerVar, serverName, serverIP, serverPing, dimensionVar);
    private final DiscordValue image = new DiscordValue("Image text", "", getValues(), menu, playerVar, serverName, serverIP, serverPing, dimensionVar);

    @Override
    public void onEnable() {
        super.onEnable();
        try {
            DiscordActivity.start(details, state, image);

            if(getMinecraft().getCurrentServerData() != null) {
                serverName.value = getMinecraft().getCurrentServerData().serverName;
                serverIP.value = getMinecraft().getCurrentServerData().serverIP;
                serverPing.value = getMinecraft().getCurrentServerData().pingToServer+"ms";
            } else if(getMinecraft().isIntegratedServerRunning()){
                serverName.value = "Singleplayer";
                serverIP.value = "Singleplayer";
                serverPing.value = "0ms";
            } else {
                serverName.value = "null";
                serverIP.value = "null";
                serverPing.value = "0ms";
            }
        } catch (RuntimeException e) {
            toggle();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        DiscordActivity.stop();
        serverName.value = "null";
        serverIP.value = "null";
        serverPing.value = "0ms";
    }

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if(getMinecraft().getCurrentServerData() != null) {
            serverPing.value = getMinecraft().getCurrentServerData().pingToServer+"ms";
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if(getMinecraft().getCurrentServerData() != null) {
            serverName.value = getMinecraft().getCurrentServerData().serverName;
            serverIP.value = getMinecraft().getCurrentServerData().serverIP;
            serverPing.value = getMinecraft().getCurrentServerData().pingToServer+"ms";
        } else if(getMinecraft().isIntegratedServerRunning()){
            serverName.value = "Singleplayer";
            serverIP.value = "Singleplayer";
            serverPing.value = "0ms";
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        serverName.value = "null";
        serverIP.value = "null";
        serverPing.value = "0ms";
    }

    @SubscribeEvent
    public void onDimensionChanged(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(event.player != getPlayer()) return;
        switch (event.toDim) {
            case 0:
                dimensionVar.value = "Overworld";
                break;
            case -1:
                dimensionVar.value = "Nether";
                break;
            case 1:
                dimensionVar.value = "End";
                break;
        }
    }

    public static class DiscordValue extends Value<String> {
        public final ChatUtil.Variable[] vars;
        private final DiscordValue menu;

        public DiscordValue(String name, String value, List<Value<?>> list, DiscordValue menu, ChatUtil.Variable... vars) {
            super(name, value, null, null, b -> true);
            this.vars = vars;
            this.menu = menu;
            list.add(this);
        }

        public String getVal() {
            if(menu != null && containsServerVar(getValue())) {
                for(ChatUtil.Variable v : vars) {
                    if(v.name.equals("${server_name}") && v.value.equals("null"))
                        return ChatUtil.replaceVars(menu.getValue());
                }
            }
            return ChatUtil.replaceVars(getValue(), vars);
        }

        private boolean containsServerVar(String s) {
            boolean a = s.contains("${server_name}");
            boolean b = s.contains("${server_ip}");
            boolean c = s.contains("${server_ping}");
            return a || b || c;
        }
    }
}
