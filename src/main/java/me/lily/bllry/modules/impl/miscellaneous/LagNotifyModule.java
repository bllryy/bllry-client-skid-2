package me.lily.bllry.modules.impl.miscellaneous;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.PacketReceiveEvent;
import me.lily.bllry.events.impl.RenderOverlayEvent;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.BooleanSetting;
import me.lily.bllry.settings.impl.ColorSetting;
import me.lily.bllry.utils.color.ColorUtils;
import me.lily.bllry.utils.system.MathUtils;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;

@RegisterModule(name = "LagNotify", description = "Notifies you when you lag.", category = Module.Category.MISCELLANEOUS)
public class LagNotifyModule extends Module {
    BooleanSetting server = new BooleanSetting("Server", "Notifies you when the server stops responding.", true);
    BooleanSetting lagback = new BooleanSetting("Lagback", "Notifies you when you lagback.", true);
    ColorSetting color = new ColorSetting("Color", "The color of the notification text.", ColorUtils.getDefaultOutlineColor());

    Vec3d lagPos = null;
    double lagDistance;
    long lagTime = System.currentTimeMillis();

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if(getNull()) return;

        if(event.getPacket() instanceof PlayerPositionLookS2CPacket packet) {
            lagPos = new Vec3d(packet.change().position().getX(), packet.change().position().getY(), packet.change().position().getZ());
            lagDistance = mc.player.getPos().distanceTo(lagPos);
            lagTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderOverlayEvent event) {
        if(getNull()) return;

        int width = mc.getWindow().getScaledWidth() / 2, height = mc.getWindow().getScaledHeight() / 4;
        boolean flag = false;

        if(server.getValue() && Bllry.SERVER_MANAGER.getResponseTimer().hasTimeElapsed(1000)) {
            String text = "Detected server not responding for " + MathUtils.round(Bllry.SERVER_MANAGER.getResponseTimer().timeElapsed()/1000f, 1) + "s.";
            Bllry.FONT_MANAGER.drawTextWithShadow(event.getContext(),text, width - Bllry.FONT_MANAGER.getWidth(text) / 2, height - Bllry.FONT_MANAGER.getHeight(), color.getColor());
            flag = true;
        }

        if(lagback.getValue() && System.currentTimeMillis() - lagTime < 3000) {
            String text = "Detected lagback of " + MathUtils.round(lagDistance, 1) + " blocks " + MathUtils.round((System.currentTimeMillis() - lagTime) / 1000f, 1) + "s.";
            Bllry.FONT_MANAGER.drawTextWithShadow(event.getContext(),text, width - Bllry.FONT_MANAGER.getWidth(text) / 2, height - Bllry.FONT_MANAGER.getHeight() + (flag ? Bllry.FONT_MANAGER.getHeight() : 0), color.getColor());
        }
    }
}
