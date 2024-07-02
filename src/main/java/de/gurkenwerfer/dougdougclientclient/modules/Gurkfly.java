package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Gurkfly implements Module {
    private boolean enabled = false;
    private double flySpeed = 2;
    private double verticalMult = 2;
    private double sprintSpeed = 2;
    private int fallTickTime = 19;
    private double noFlyDist = 0.03127;

    MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public void initialize() {
        if (client.player != null){
            System.out.println("Gurkfly enabled!");
            client.player.sendMessage(Text.of("Gurkfly enabled!"));
            ClientTickEvents.START_CLIENT_TICK.register(this::onStartTick);
        }
    }

    @Override
    public void terminate() {
        if (client.player != null){
            System.out.println("Gurkfly disabled!");
            client.player.sendMessage(Text.of("Gurkfly disabled!"));
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

    // The listener method
    private void onStartTick(MinecraftClient client) {
        // Your code to run at the start of each client tick
        if (client.player != null && isEnabled()) {

            float yaw = client.player.getYaw();
            double yawRadians = Math.toRadians(yaw);

            // Calculate a normalized vector in the direction of the yaw
            double cameraX = -Math.sin(yawRadians);
            double cameraZ = Math.cos(yawRadians);

            // Create a Vec3d representing the camera direction
            Vec3d cameraDirection = new Vec3d(cameraX, 0, cameraZ).normalize();
            Vec3i playerVector = new Vec3i(0, (int) client.player.getY(), 0);

            // Initialize the movement direction vector
            Vec3d movementDirection = new Vec3d(0, 0, 0);

            if ((!client.player.isOnGround() && client.player != null)) {
                // Check for player input and adjust the movement direction vector accordingly
                if (client.options.backKey.isPressed()) {
                    movementDirection = movementDirection.add(cameraDirection.multiply(-flySpeed));
                }
                if (client.options.forwardKey.isPressed()) {
                    movementDirection = movementDirection.add(cameraDirection.multiply(flySpeed));
                }
                if (client.options.leftKey.isPressed()) {
                    // Rotate the movement direction vector 90 degrees counterclockwise
                    movementDirection = movementDirection.add(cameraDirection.rotateY((float) (Math.PI / 2)).normalize().multiply(flySpeed));
                }
                if (client.options.rightKey.isPressed()) {
                    // Rotate the movement direction vector 90 degrees clockwise
                    movementDirection = movementDirection.add(cameraDirection.rotateY((float) (-Math.PI / 2)).normalize().multiply(flySpeed));
                }
                if (client.options.jumpKey.isPressed()) {
                    movementDirection = movementDirection.add(0, verticalMult, 0);
                    playerVector.add(2,2,1);
                }
                if (client.options.sneakKey.isPressed()) {
                    movementDirection = movementDirection.add(0, -verticalMult, 0);
                    playerVector.add(2,-2,1);
                }
                if (client.options.sprintKey.isPressed()) {
                    movementDirection = movementDirection.multiply(sprintSpeed);
                }

                // Set the player's velocity using the movement direction vector
                client.player.setVelocity(movementDirection);

                if (client.player.age % fallTickTime == 0) {
                    if (client.options.jumpKey.isPressed()) {
                        client.player.setPosition(client.player.getX(), client.player.getY() - (noFlyDist * verticalMult), client.player.getZ());
                    } else {
                        client.player.setPosition(client.player.getX(), client.player.getY() - noFlyDist, client.player.getZ());
                    }

                } else {
                    client.player.setPosition(client.player.getX(), client.player.getY() + (noFlyDist / (fallTickTime - 1)), client.player.getZ());
                }

            }
        }
    }

    @Override
    public void buildConfigEntries(ConfigCategory category, ConfigEntryBuilder builder) {
        category.addEntry(builder.startBooleanToggle(Text.of("Enable/Disable The Module"), enabled)
                .setSaveConsumer(value -> enabled = value)
                .build());

        category.addEntry(builder.startDoubleField(Text.of("Fly Speed"), flySpeed)
                .setSaveConsumer(value -> flySpeed = value)
                .build());

        category.addEntry(builder.startDoubleField(Text.of("Vertical Multiplier"), verticalMult)
                .setSaveConsumer(value -> verticalMult = value)
                .build());

        category.addEntry(builder.startIntField(Text.of("Fall Tick Time"), fallTickTime)
                .setSaveConsumer(value -> fallTickTime = value)
                .build());

        category.addEntry(builder.startDoubleField(Text.of("No Fly Distance"), noFlyDist)
                .setSaveConsumer(value -> noFlyDist = value)
                .build());
    }
}
