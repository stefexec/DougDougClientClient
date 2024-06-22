package de.gurkenwerfer.dougdougclientclient.classes;

import com.llamalad7.mixinextras.ap.StdoutMessager;
import de.gurkenwerfer.dougdougclientclient.modules.ModuleA;
import de.gurkenwerfer.dougdougclientclient.modules.ModuleB;
import de.gurkenwerfer.dougdougclientclient.modules.ModuleC;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    private static final Map<String, Module> moduleMap = new HashMap<>();

    // Method to initialize modules and their default states
    public static void initializeModules() {
        // Example: Initialize modules and their default states
        moduleMap.put("ModuleA", new ModuleA());
        moduleMap.put("ModuleB", new ModuleB());
        moduleMap.put("ModuleC", new ModuleC());

        // Add more modules as needed
        for (Module module : moduleMap.values()) {
            if (ModConfig.load().isModuleEnabled(module.toString())) {
                module.initialize();
            }
        }
    }

    // Method to get module names
    public static String[] getModuleNames() {
        return moduleMap.keySet().toArray(new String[0]);
    }


}
