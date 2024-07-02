package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class NoFall implements Module {

    boolean enabled = true; // Default to enabled
    MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void initialize() {
        if (mc.player != null) {
            mc.player.sendMessage(Text.of("NoFall enabled!"));

            // Register the client tick event to set up packet interception
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                // Packet handling logic is now within the mixin
            });
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

    @Override
    public void buildConfigEntries(ConfigCategory category, ConfigEntryBuilder builder) {
        category.addEntry(builder.startBooleanToggle(Text.of("Enable/Disable The Module"), enabled)
                .setSaveConsumer(value -> enabled = value)
                .build());
    }

}
