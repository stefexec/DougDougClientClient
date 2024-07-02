package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import de.gurkenwerfer.dougdougclientclient.classes.ModuleManager;
import de.gurkenwerfer.dougdougclientclient.mixin.ClientConnectionAccessor;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;


public class MessyTp implements Module {
    boolean enabled = false;

    private static final int MIN_Y = -60;
    private static final int MAX_Y = 320;
    MinecraftClient mc = MinecraftClient.getInstance();

    BlockPos playerPos;
    int y;
    BlockPos blockBelowPos;
    BlockPos blockAbovePos;

    @Override
    public void initialize() {
        if (mc.player != null) {
            playerPos = mc.player.getBlockPos();
            y = playerPos.getY();
            blockBelowPos = findAirBelowPlayer(playerPos);
            blockAbovePos = findAirAbovePlayer(playerPos);
            ClientTickEvents.END_CLIENT_TICK.register(this::onEndTick);
        }
    }

    @Override
    public void terminate() {
        assert mc.player != null;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private void onEndTick(MinecraftClient mc) {
        if (mc.player != null && isEnabled()) {
            int teleportDistance;

            if (mc.options.sneakKey.isPressed() && blockBelowPos != null) {
                y = blockBelowPos.down().getY();
                teleportDistance = playerPos.getY() - y;
                mc.player.sendMessage(Text.of("Air block is " + teleportDistance + " below you"));

            } else if (!mc.options.sneakKey.isPressed() && blockAbovePos != null) {
                y = blockAbovePos.down().getY();
                teleportDistance = y - playerPos.getY();
                mc.player.sendMessage(Text.of("Air block is " + teleportDistance + " above you"));
            } else {
                mc.player.sendMessage(Text.of("Could not find a suitable teleportation spot"));
                teleportDistance = 0;
            }

            int packetsRequired = (int) Math.ceil(Math.abs(teleportDistance / 10));
            if (mc.options.sneakKey.isPressed()) {
                teleportDistance = -teleportDistance;
            }
            for (int packetNum = 0; packetNum <= (packetsRequired - 1); packetNum++) {

                PlayerMoveC2SPacket currentPosition = new PlayerMoveC2SPacket.Full(
                        Math.floor(mc.player.getX()) + 0.500,
                        mc.player.getY(),
                        Math.floor(mc.player.getZ()) + 0.500,
                        mc.player.getYaw(0),
                        mc.player.getPitch(0),
                        true);

                ((ClientConnectionAccessor) mc.getNetworkHandler().getConnection())._send(currentPosition, null);
                //System.out.println("Sent packet " + packetNum + " of " + packetsRequired + " packets");
            }

            PlayerMoveC2SPacket newPosition = new PlayerMoveC2SPacket.Full(
                    Math.floor(mc.player.getX()) + 0.500,
                    mc.player.getY() + teleportDistance,
                    Math.floor(mc.player.getZ()) + 0.500,
                    mc.player.getYaw(0),
                    mc.player.getPitch(0),
                    true);
            mc.player.setPosition(Math.floor(mc.player.getX()) + 0.500, mc.player.getY() + teleportDistance, Math.floor(mc.player.getZ()) + 0.500);
            ((ClientConnectionAccessor) mc.getNetworkHandler().getConnection())._send(newPosition, null);
            ModuleManager.disableModule("MessyTp");
        }
    }

    public BlockPos findAirBelowPlayer(BlockPos playerPos) {
        assert mc.world != null;
        BlockPos currentPos = playerPos.down();
        BlockPos lastAirBlock2 = null;

        while (currentPos.getY() > MIN_Y) {
            if (mc.world.getBlockState(currentPos).isAir() && mc.world.getBlockState(currentPos.down()).isAir() && !mc.world.getBlockState(currentPos.down().down()).isAir()) {
                lastAirBlock2 = currentPos;
                currentPos = currentPos.down(1000);
            }
            currentPos = currentPos.down();
        }
        return lastAirBlock2;
    }

    public BlockPos findAirAbovePlayer(BlockPos playerPos) {
        assert mc.world != null;
        BlockPos currentPos = playerPos.up(3); //Need to check three blocks up to prevent false detections such as standing on redstone
        BlockPos lastAirBlock2 = null;

        while (currentPos.getY() < MAX_Y) {
            if (mc.world.getBlockState(currentPos).isAir() && mc.world.getBlockState(currentPos.down()).isAir() && !mc.world.getBlockState(currentPos.down().down()).isAir()) {
                lastAirBlock2 = currentPos;
                currentPos = currentPos.up(1000);
            }
            currentPos = currentPos.up();
        }
        return lastAirBlock2;
    }

    @Override
    public void buildConfigEntries(ConfigCategory category, ConfigEntryBuilder builder) {
        category.addEntry(builder.startBooleanToggle(Text.of("Enable/Disable The Module"), enabled)
                .setSaveConsumer(value -> enabled = value)
                .build());
    }
}
