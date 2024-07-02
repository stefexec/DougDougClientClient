package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class AirJump implements Module {

    boolean enabled = false; // Default to enabled
    int level = 0;
    boolean maintainLevel = false; // make toggleable in modmenu maybe

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

    @Override
    public void buildConfigEntries(ConfigCategory category, ConfigEntryBuilder builder) {
        category.addEntry(builder.startBooleanToggle(Text.of("Enable/Disable The Module"), enabled)
                .setSaveConsumer(value -> enabled = value)
                .build());
        category.addEntry(builder.startBooleanToggle(Text.of("Maintain Level"), maintainLevel)
                .setSaveConsumer(value -> maintainLevel = value)
                .build());
    }

}
