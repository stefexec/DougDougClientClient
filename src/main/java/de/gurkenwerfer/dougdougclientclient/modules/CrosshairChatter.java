package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CrosshairChatter implements Module {
    boolean enabled = true; // Default to enabled

    @Override
    public void initialize() {
        // Register the event listener
        ClientTickEvents.START_CLIENT_TICK.register(this::onStartTick);
    }

    // The listener method
    private void onStartTick(MinecraftClient mc) {
        // Your code to run at the start of each client tick
        if(mc.player != null) {
            if (mc.targetedEntity != null) {
                //mc.player.sendMessage(mc.targetedEntity.getName(), false);
            }
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

    @Override
    public void buildConfigEntries(ConfigCategory category, ConfigEntryBuilder builder) {
        category.addEntry(builder.startBooleanToggle(Text.of("Enable/Disable The Module"), enabled)
                .setSaveConsumer(value -> enabled = value)
                .build());
    }

}
