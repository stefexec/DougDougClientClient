package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CrosshairChatter implements Module {
    private boolean enabled = true; // Default to enabled

    @Override
    public void initialize() {
        // Register the event listener
        ClientTickEvents.START_CLIENT_TICK.register(this::onStartTick);
    }

    // The listener method
    private void onStartTick(MinecraftClient mc) {
        // Your code to run at the start of each client tick
        if (mc.player != null) {
            mc.player.sendMessage(Text.of(mc.targetedEntity.getEntityName()), false);
        }
    }

    @Override
    public void terminate() {
        System.out.println("ModuleC terminated!");
        // Your module-specific termination code here
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
