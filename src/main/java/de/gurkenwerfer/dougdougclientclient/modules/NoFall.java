package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class NoFall implements Module {

    private boolean enabled = true; // Default to enabled
    MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void initialize() {
        if (mc.player != null) {
            mc.player.sendMessage(Text.of("NoFall enabled!"));
        }

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
