package de.gurkenwerfer.dougdougclientclient.classes;

import de.gurkenwerfer.dougdougclientclient.modules.*;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    private static final Map<String, Module> moduleMap = new HashMap<>();
    private static ModConfig config;

    // Method to initialize modules and their default states
    public static void initializeModules() {
        // Load the configuration once
        config = ModConfig.load();

        // Example: Initialize modules and their default states
        moduleMap.put("Gurkfly", new Gurkfly());
        moduleMap.put("Gurkreach", new Gurkreach());
        moduleMap.put("MessyTp", new MessyTp());
        moduleMap.put("CrosshairChatter", new CrosshairChatter());
        moduleMap.put("NoFall", new NoFall());
        moduleMap.put("AirJump", new AirJump());

        // Add more modules as needed
        for (Map.Entry<String, Module> entry : moduleMap.entrySet()) {
            String moduleName = entry.getKey();
            Module module = entry.getValue();
            if (config.isModuleEnabled(moduleName)) {
                module.initialize();
            }
        }
    }

    public static void onGameJoin() {
        config = ModConfig.load();
        for (Map.Entry<String, Boolean> entry : config.getModuleEnabledMap().entrySet()) {
            String moduleName = entry.getKey();
            boolean isEnabled = entry.getValue();
            if (isEnabled) {
                enableModule(moduleName);
            } else {
                disableModule(moduleName);
            }
        }
    }

    public static Module getModule(String moduleName) {
        return moduleMap.get(moduleName);
    }

    // Method to get module names
    public static String[] getModuleNames() {
        return moduleMap.keySet().toArray(new String[0]);
    }

    // Method to enable a module
    public static void enableModule(String moduleName) {
        Module module = moduleMap.get(moduleName);
        if (module != null && !module.isEnabled()) {
            module.setEnabled(true);
            module.initialize();
            config.setModuleEnabled(moduleName);
            config.save();
        }
    }

    // Method to disable a module
    public static void disableModule(String moduleName) {
        Module module = moduleMap.get(moduleName);
        if (module != null && module.isEnabled()) {
            module.terminate();
            module.setEnabled(false);
            config.setModuleDisabled(moduleName);
            config.save();
        }
    }

    public static boolean isModuleEnabled(String moduleName) {
        Module module = moduleMap.get(moduleName);
        return module != null && module.isEnabled();
    }

    public static void toggleModule(String moduleName) {
        if (isModuleEnabled(moduleName)) {
            disableModule(moduleName);
        } else {
            enableModule(moduleName);
        }
    }
}
