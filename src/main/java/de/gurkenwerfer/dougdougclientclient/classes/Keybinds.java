package de.gurkenwerfer.dougdougclientclient.classes;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class Keybinds {
    private static final Map<String, KeyBinding> keyBindings = new HashMap<>();
    private static KeyBinding openConfigKey;

    public static void register() {
        openConfigKey = registerKeyBinding(
                "key.dougdougclientclient.open_config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.dougdougclientclient.keybindings"
        );

        // Assign numpad keys to each module
        int key = GLFW.GLFW_KEY_KP_1; // Start with the numpad 1 key

        for (String moduleName : ModuleManager.getModuleNames()) {
            KeyBinding keyBinding = registerKeyBinding(
                    "key.dougdougclientclient.toggle_" + moduleName.toLowerCase(),
                    InputUtil.Type.KEYSYM,
                    key,
                    "category.dougdougclientclient.keybindings"
            );
            keyBindings.put(moduleName, keyBinding);
            System.out.println("Registered keybind for module: " + moduleName);
            key++;
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openConfigKey.wasPressed()) {
                MinecraftClient.getInstance().setScreen(MyConfigScreen.getConfigScreen(null));
            }

            if (client.player != null && client.currentScreen == null) {
                for (Map.Entry<String, KeyBinding> entry : keyBindings.entrySet()) {
                    String moduleName = entry.getKey();
                    KeyBinding keyBinding = entry.getValue();
                    if (keyBinding.wasPressed()) {
                        System.out.println("Keybind was pressed for module: " + moduleName);
                        ModuleManager.toggleModule(moduleName);
                    }
                }
            }
        });
    }

    private static KeyBinding registerKeyBinding(String id, InputUtil.Type type, int defaultKey, String category) {
        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                id,
                type,
                defaultKey,
                category
        ));
        return keyBinding;
    }
}