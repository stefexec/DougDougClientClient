package de.gurkenwerfer.dougdougclientclient.classes;

import de.gurkenwerfer.dougdougclientclient.modules.CrosshairChatter;
import de.gurkenwerfer.dougdougclientclient.modules.Gurkfly;
import de.gurkenwerfer.dougdougclientclient.modules.Gurkreach;
import de.gurkenwerfer.dougdougclientclient.modules.MessyTp;



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

    // Method to enable a module
    public static void enableModule(String moduleName) {
        Module module = moduleMap.get(moduleName);
        if (module != null && !module.isEnabled()) {
            module.setEnabled(true);
            module.initialize();
        }
    }

    // Method to disable a module
    public static void disableModule(String moduleName) {
        Module module = moduleMap.get(moduleName);
        if (module != null && module.isEnabled()) {
            module.terminate();
            module.setEnabled(false);
        }
    }

    public static boolean isModuleEnabled(String moduleName) {
        Module module = moduleMap.get(moduleName);
        return module != null && module.isEnabled();
    }

}