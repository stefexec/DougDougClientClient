package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import de.gurkenwerfer.dougdougclientclient.classes.ModuleManager;
import de.gurkenwerfer.dougdougclientclient.mixin.ClientConnectionAccessor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;


public class MessyTp implements Module {
    private boolean enabled = false;
    MinecraftClient client = MinecraftClient.getInstance();

    BlockPos playerPos;
    int y;
    BlockPos blockBelowPos;
    BlockPos blockAbovePos;

    @Override
    public void initialize() {
        assert client.player != null;
        playerPos = client.player.getBlockPos();
        y = playerPos.getY();
        blockBelowPos = findAirBelowPlayer(playerPos);
        blockAbovePos = findAirAbovePlayer(playerPos);
        ClientTickEvents.START_CLIENT_TICK.register(this::onStartTick);
    }

    @Override
    public void terminate() {
        assert client.player != null;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private void onStartTick(MinecraftClient mc) {
        if (mc.player != null && isEnabled()) {
            int teleportDistance;

            if (mc.options.sneakKey.isPressed() && blockBelowPos != null) {
                y = blockBelowPos.down().getY();
                teleportDistance = playerPos.getY() - y;
                // good o' paperclip
                int packetsRequired = (int) Math.ceil(Math.abs(teleportDistance / 10));
                mc.player.sendMessage(Text.of("Air block is " + teleportDistance + " below you"));

                for (int packetNum = 0; packetNum < (packetsRequired - 1); packetNum++) {

                    PlayerMoveC2SPacket currentPosition  = new PlayerMoveC2SPacket.Full(
                            mc.player.getX(),
                            mc.player.getY(),
                            mc.player.getZ(),
                            mc.player.getYaw(0),
                            mc.player.getPitch(0),
                            true);

                    ((ClientConnectionAccessor) mc.getNetworkHandler().getConnection())._send(currentPosition, null);
                }

                PlayerMoveC2SPacket newPosition = new PlayerMoveC2SPacket.Full(
                        mc.player.getX(),
                        mc.player.getY() - teleportDistance,
                        mc.player.getZ(),
                        mc.player.getYaw(0),
                        mc.player.getPitch(0),
                        true);

                ((ClientConnectionAccessor) mc.getNetworkHandler().getConnection())._send(newPosition, null);
                mc.player.setPosition(mc.player.getX(), mc.player.getY() - teleportDistance, mc.player.getZ());

                ModuleManager.disableModule("MessyTp");

            } else if (!mc.options.sneakKey.isPressed() && blockAbovePos != null) {

                y = blockAbovePos.up().getY();
                teleportDistance = playerPos.getY() - y;
                teleportDistance = -teleportDistance;
                // good o' paperclip
                int packetsRequired = (int) Math.ceil(Math.abs(teleportDistance / 10));
                mc.player.sendMessage(Text.of("Air block is " + teleportDistance + " above you"));

                for (int packetNum = 0; packetNum < (packetsRequired - 1); packetNum++) {
                    PlayerMoveC2SPacket currentPosition  = new PlayerMoveC2SPacket.Full(
                            mc.player.getX(),
                            mc.player.getY(),
                            mc.player.getZ(),
                            mc.player.getYaw(0),
                            mc.player.getPitch(0),
                            true);

                    ((ClientConnectionAccessor) mc.getNetworkHandler().getConnection())._send(currentPosition, null);
                }

                PlayerMoveC2SPacket newPosition = new PlayerMoveC2SPacket.Full(
                        mc.player.getX(),
                        mc.player.getY() - teleportDistance + 1,
                        mc.player.getZ(),
                        mc.player.getYaw(0),
                        mc.player.getPitch(0),
                        true);

                ((ClientConnectionAccessor) mc.getNetworkHandler().getConnection())._send(newPosition, null);
                mc.player.setPosition(mc.player.getX(), mc.player.getY() + teleportDistance, mc.player.getZ());

                ModuleManager.disableModule("MessyTp");

            } else {

                mc.player.sendMessage(Text.of("Could not find a suitable teleportation spot"));
                ModuleManager.disableModule("MessyTp");

            }
        }
    }

    public BlockPos findAirBelowPlayer(BlockPos playerPos) {
        assert client.world != null;
        BlockPos currentPos = playerPos.down();
        BlockPos lastAirBlock2 = null;

        while (currentPos.getY() > - 60) {
            if (client.world.getBlockState(currentPos).isAir() && client.world.getBlockState(currentPos.down()).isAir() && !client.world.getBlockState(currentPos.down().down()).isAir()) {
                lastAirBlock2 = currentPos;
                currentPos = currentPos.down(320);
            }
            currentPos = currentPos.down();
        }
        return  lastAirBlock2;
    }

    public BlockPos findAirAbovePlayer(BlockPos playerPos) {
        assert client.world != null;
        BlockPos currentPos = playerPos.up().up().up(); //Need to check three blocks up to prevent false detections such as standing on redstone
        BlockPos lastAirBlock2 = null;

        while (currentPos.getY() < 320) {
            if (client.world.getBlockState(currentPos).isAir() && client.world.getBlockState(currentPos.down()).isAir() && !client.world.getBlockState(currentPos.down().down()).isAir()) {
                lastAirBlock2 = currentPos;
                currentPos = currentPos.up(320);
            }
            currentPos = currentPos.up();
        }
        return  lastAirBlock2;
    }
}
