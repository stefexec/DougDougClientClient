package de.gurkenwerfer.dougdougclientclient.utils;

import de.gurkenwerfer.dougdougclientclient.mixin.ClientConnectionAccessor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;


public class PacketHelper {

    public static int tickCounter = 0;

    static {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            tickCounter++;
            if (tickCounter == 20) {
                tickCounter = 0;
            }
        });
    }

    public static void teleportFromTo(MinecraftClient mcClient, Vec3d from, Vec3d to) {
        MinecraftClient mc = MinecraftClient.getInstance();
        assert mc.player != null;
        double maxDist = 5;
        double totalDist = from.distanceTo(to);
        double targetDist = Math.ceil(totalDist / maxDist);

            for (int i = 1; i <= targetDist; i++) {

                double x = from.x + (to.x - from.x) / targetDist * i;
                double y = from.y + (to.y - from.y) / targetDist * i;
                double z = from.z + (to.z - from.z) / targetDist * i;

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
