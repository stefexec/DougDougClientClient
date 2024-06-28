package de.gurkenwerfer.dougdougclientclient.utils;

import de.gurkenwerfer.dougdougclientclient.mixin.ClientConnectionAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;


public class PacketHelper {

    public static void teleportFromTo(MinecraftClient client, Vec3d from, Vec3d to) {
        MinecraftClient mc = MinecraftClient.getInstance();
        assert mc.player != null;
        double maxDistPerTp = 5;
        double totalDist = from.distanceTo(to);
        double numOfTp = Math.ceil(totalDist / maxDistPerTp);

            for (int i = 1; i <= numOfTp; i++) {

                double x = from.x + (to.x - from.x) / numOfTp * i;
                double y = from.y + (to.y - from.y) / numOfTp * i;
                double z = from.z + (to.z - from.z) / numOfTp * i;

                Packet<?> packet = new PlayerMoveC2SPacket.PositionAndOnGround(
                        x,
                        y + 1,
                        z,
                        mc.player.isOnGround()
                );

                ((ClientConnectionAccessor) mc.getNetworkHandler().getConnection())._send(packet, null);
            }
    }
}
