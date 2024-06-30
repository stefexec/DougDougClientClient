package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Fullbright implements Module {
    private boolean enabled = false;
    MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void initialize() {
        System.out.println("Fullbright initialized!");
        mc.player.sendMessage(Text.of("Fullbright enabled!"));
    }

    @Override
    public void terminate() {
        System.out.println("Fullbright terminated!");
        mc.player.sendMessage(Text.of("Fullbright disabled!"));
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
