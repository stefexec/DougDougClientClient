package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import de.gurkenwerfer.dougdougclientclient.classes.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Gurkreach implements Module {
    private boolean enabled = false;
    MinecraftClient mc = MinecraftClient.getInstance();

    float reach = 120;

    int tick = 0;

    @Override
    public void initialize() {
        System.out.println("Gurkreach initialized!");
        mc.player.sendMessage(Text.of("Gurkreach enabled!"));
    }

    @Override
    public void terminate() {
        System.out.println("Gurkreach terminated!");
        mc.player.sendMessage(Text.of("Gurkreach disabled!"));
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

    public float getReach() {
        if (!ModuleManager.isModuleEnabled("Gurkreach")) return mc.interactionManager.getCurrentGameMode().isCreative() ? 5.0F : 4.5F;
        return reach;
    }
}
