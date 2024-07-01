package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import de.gurkenwerfer.dougdougclientclient.classes.ModuleManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

public class Notifier implements Module {
    private boolean enabled = false;
    private Set<Entity> previouslyRenderedEntities = new HashSet<>();
    private Set<Entity> currentRenderedEntities = new HashSet<>();
    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void initialize() {
        System.out.println("Notifier initialized!");

        ClientTickEvents.START_CLIENT_TICK.register(this::onStartTick);
    }

    @Override
    public void terminate() {
        System.out.println("Notifier terminated!");
        previouslyRenderedEntities.clear();
    }

    private void onStartTick(MinecraftClient client) {
        if (ModuleManager.isModuleEnabled("Notifier")) {
            if (client.world == null) {
                return;
            }

            Set<Entity> entitiesInWorld = new HashSet<>();
            for (Entity entity : client.world.getEntities()) {
                if (!entity.getUuid().equals(mc.player.getUuid()) && entity instanceof PlayerEntity) {
                    entitiesInWorld.add(entity);
                }
            }

            // Players that just arrived
            for (Entity entity : entitiesInWorld) {
                if (!currentRenderedEntities.contains(entity)) {
                    mc.player.sendMessage(Text.of(entity.getName().getString() + " is nearby!"));
                    mc.world.playSoundFromEntity(mc.player, mc.player, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 3.0F, 1.0F);
                    currentRenderedEntities.add(entity);
                }
            }

            // Players that just left
            currentRenderedEntities.removeIf(entity -> {
                if (!entitiesInWorld.contains(entity)) {
                    mc.player.sendMessage(Text.of(entity.getName().getString() + " is no longer nearby!"));
                    mc.world.playSoundFromEntity(mc.player, mc.player, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 3.0F, 1.0F);
                    return true;
                }
                return false;
            });
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