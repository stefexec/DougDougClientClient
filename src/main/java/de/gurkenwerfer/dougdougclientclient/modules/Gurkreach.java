package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import de.gurkenwerfer.dougdougclientclient.classes.ModuleManager;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Gurkreach implements Module {
    boolean enabled = false;
    MinecraftClient mc = MinecraftClient.getInstance();

    float reach = 120;

    int tick = 0;

    @Override
    public void initialize() {
        if (mc.player == null) {
            System.err.println("Cannot initialize Gurkreach: Minecraft client or player is null.");
            return;
        }
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

    @Override
    public void buildConfigEntries(ConfigCategory category, ConfigEntryBuilder builder) {
        category.addEntry(builder.startBooleanToggle(Text.of("Enable/Disable The Module"), enabled)
                .setSaveConsumer(value -> enabled = value)
                .build());
    }
}
