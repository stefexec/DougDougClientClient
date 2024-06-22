package de.gurkenwerfer.dougdougclientclient.classes;

import de.gurkenwerfer.dougdougclientclient.modules.ModuleA;
import de.gurkenwerfer.dougdougclientclient.modules.ModuleB;
import de.gurkenwerfer.dougdougclientclient.modules.ModuleC;

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
        moduleMap.put("ModuleA", new ModuleA());
        moduleMap.put("ModuleB", new ModuleB());
        moduleMap.put("ModuleC", new ModuleC());

        // Add more modules as needed
        for (Map.Entry<String, Module> entry : moduleMap.entrySet()) {
            String moduleName = entry.getKey();
            Module module = entry.getValue();
            if (config.isModuleEnabled(moduleName)) {
                module.initialize();
            }
        }
    }

    // Method to get module names
    public static String[] getModuleNames() {
        return moduleMap.keySet().toArray(new String[0]);
    }

    public static void saveConfig() {
        if (config != null) {
            config.save();
        }
    }
}