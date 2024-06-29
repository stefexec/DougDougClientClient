package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class AirJump implements Module {

    private boolean enabled = true; // Default to enabled
    private int level = 0;
    private boolean maintainLevel = false; // make toggleable in modmenu maybe

    MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void initialize() {
        if (mc.player != null){
            mc.player.sendMessage(Text.of("AirJump enabled!"));
        }
        ClientTickEvents.START_CLIENT_TICK.register(this::onTick);
        ClientTickEvents.END_CLIENT_TICK.register(this::onKey);
        setEnabled(true);
    }

    private void onKey(MinecraftClient mc) {
        if(enabled) {
            if (mc.player == null || mc.world == null) return;
            if (mc.player.isOnGround()) return;

            if (mc.options.jumpKey.isPressed()) {
                level = mc.player.getBlockPos().getY();
                mc.player.jump();
            } else if (mc.options.sneakKey.isPressed()) {
                level--;
            }
        }
    }

    private void onTick(MinecraftClient mc) {
        if(enabled){
            if (mc.player == null || mc.world == null) return;
            if (mc.player.isOnGround()) return;

            if (maintainLevel && mc.player.getBlockPos().getY() == level && mc.options.jumpKey.isPressed()) {
                mc.player.jump();
            }
        }
    }

    @Override
    public void terminate() {
        if (mc.player != null){
            mc.player.sendMessage(Text.of("AirJump disabled!"));
        }
        setEnabled(false);
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
