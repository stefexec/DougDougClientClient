package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import de.gurkenwerfer.dougdougclientclient.mixin.PlayerMoveC2SPacketAccessor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.networking.client.ClientNetworkingImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;

public class NoFall implements Module {

    private boolean enabled = true; // Default to enabled
    MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void initialize() {
        if (mc.player != null) {
            mc.player.sendMessage(Text.of("NoFall enabled!"));

            // Register the client tick event to set up packet interception
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                // Packet handling logic is now within the mixin
            });
        }
    }

    private void handlePlayerMovePacket(PlayerMoveC2SPacket packet, ClientPlayerEntity player) {
        if (shouldModifyPacket(player)) {
            // Use the mixin accessor to modify the packet
            ((PlayerMoveC2SPacketAccessor) packet).setOnGround(true);
        }
    }

    private boolean shouldModifyPacket(ClientPlayerEntity player) {
        // Your conditions to check if the packet should be modified
        return !player.getAbilities().creativeMode && player.getVelocity().y <= -0.5;
    }

    @Override
    public void terminate() {
        if (mc.player != null) {
            mc.player.sendMessage(Text.of("NoFall disabled!"));
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
