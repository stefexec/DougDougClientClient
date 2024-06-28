package de.gurkenwerfer.dougdougclientclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {


    @Inject(method = "sendPacket*", at = @At("HEAD"))
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        //String p1 = ((ClientPlayNetworkHandler) (Object) this).getProfile().getId().toString();
        // Your logic to modify the packet
        if (packet instanceof PlayerMoveC2SPacket) {
            if (mc.player != null && shouldModifyPacket(mc.player)) {
                mc.player.sendMessage(Text.of("I'm falling!"));
                ((PlayerMoveC2SPacketAccessor) packet).setOnGround(true);
            }
        }

    }

    @Unique
    private boolean shouldModifyPacket(ClientPlayerEntity player) {
        // Your conditions to check if the packet should be modified
        return !player.getAbilities().creativeMode && player.getVelocity().y <= -0.5;
    }
}