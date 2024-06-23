package de.gurkenwerfer.dougdougclientclient.mixin;

import io.netty.channel.Channel;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.PacketCallbacks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientConnection.class)
public interface ClientConnectionAccessor {
    @Accessor("channel")
    Channel getChannel();

    // THIS IS < 1.20.1
    //@Invoker("sendImmediately")
    //void _sendImmediately(Packet<?> packet, @Nullable PacketCallbacks callbacks);

    @Invoker("send")
    void _send(Packet<?> packet, @Nullable PacketCallbacks callbacks);
}
