package de.gurkenwerfer.dougdougclientclient.mixin;

import de.gurkenwerfer.dougdougclientclient.classes.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow public ServerPlayerEntity player;
    MinecraftClient mc = MinecraftClient.getInstance();

    @Inject(method = "onPlayerMove", at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        if (packet != null) {
            if (ModuleManager.isModuleEnabled("NoFall")) {
                if (mc.player != null) {
                    if (!mc.player.getAbilities().creativeMode && mc.player.getVelocity().y > -0.5) {
                        mc.player.sendMessage(Text.of("Im Falling!"));
                        ((PlayerMoveC2SPacketAccessor) packet).setOnGround(true);
                    }
                }
            }
        }
    }
}