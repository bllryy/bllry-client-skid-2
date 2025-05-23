package me.lily.bllry.modules.impl.player;

import me.lily.bllry.Bllry;
import me.lily.bllry.events.SubscribeEvent;
import me.lily.bllry.events.impl.PacketReceiveEvent;
import me.lily.bllry.mixins.accessors.PlayerPositionAccessor;
import me.lily.bllry.modules.Module;
import me.lily.bllry.modules.RegisterModule;
import me.lily.bllry.settings.impl.BooleanSetting;
import me.lily.bllry.utils.minecraft.PositionUtils;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;

@RegisterModule(name = "NoRotate", description = "Prevents the server from forcing rotations on you.", category = Module.Category.PLAYER)
public class NoRotateModule extends Module {
    public BooleanSetting inBlocks = new BooleanSetting("InBlocks", "Whether or not to stop rotations whenever inside of a block.", false);
    public BooleanSetting spoof = new BooleanSetting("Spoof", "Sends rotation packets once you have been rubberbanded.", false);

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (!inBlocks.getValue() && !mc.world.getBlockState(PositionUtils.getFlooredPosition(mc.player)).isReplaceable()) return;

        if (event.getPacket() instanceof PlayerPositionLookS2CPacket packet) {
            if (spoof.getValue()) {
                Bllry.ROTATION_MANAGER.packetRotate(packet.change().yaw(), packet.change().pitch());
                Bllry.ROTATION_MANAGER.packetRotate(mc.player.getYaw(), mc.player.getPitch());
            }

            ((PlayerPositionAccessor) (Object) packet.change()).setYaw(mc.player.getYaw());
            ((PlayerPositionAccessor) (Object) packet.change()).setPitch(mc.player.getPitch());

            packet.relatives().remove(PositionFlag.X_ROT);
            packet.relatives().remove(PositionFlag.Y_ROT);
        }
    }
}
