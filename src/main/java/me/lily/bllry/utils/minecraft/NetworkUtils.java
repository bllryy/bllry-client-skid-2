package me.lily.bllry.utils.minecraft;

import me.lily.bllry.mixins.accessors.ClientWorldAccessor;
import me.lily.bllry.utils.IMinecraft;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.*;

public class NetworkUtils implements IMinecraft {
    public static void sendIgnoredPacket(Packet<?> packet) {
        mc.getNetworkHandler().getConnection().send(packet, null, true);
    }

    public static void sendSequencedPacket(SequencedPacketCreator packetCreator) {
        try (PendingUpdateManager pendingUpdateManager = ((ClientWorldAccessor)mc.world).invokeGetPendingUpdateManager().incrementSequence();){
            Packet<ServerPlayPacketListener> packet = packetCreator.predict(pendingUpdateManager.getSequence());
            mc.getNetworkHandler().sendPacket(packet);
        }
    }
}
