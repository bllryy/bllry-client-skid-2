package me.lily.bllry.modules.impl.miscellaneous;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.PacketReceiveEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.StringSetting;
import me.lily.bllry.utils.system.Timer;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

@RegisterModule(name = "AutoLogin", description = "Automatically logs in on cracked servers.", category = Module.Category.MISCELLANEOUS)
public class AutoLoginModule extends Module {
    public StringSetting password = new StringSetting("Password", "The password to use when logging in.", "password");

    private final Timer timer = new Timer();

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if(getNull() || !timer.hasTimeElapsed(10000)) return;

        if(event.getPacket() instanceof GameMessageS2CPacket packet) {
            String s = packet.content().getString().toLowerCase();

            if(s.contains("/register")) {
                mc.getNetworkHandler().sendChatCommand("register " + password.getValue() + " " + password.getValue());
                Bllry.CHAT_MANAGER.tagged("Registered successfully.", getName());
                timer.reset();
            } else if(s.contains("/login")) {
                mc.getNetworkHandler().sendChatCommand("login " + password.getValue());
                Bllry.CHAT_MANAGER.tagged("Logged in as " + mc.getSession().getUsername() + ".", getName());
                timer.reset();
            }
        }
    }
}
