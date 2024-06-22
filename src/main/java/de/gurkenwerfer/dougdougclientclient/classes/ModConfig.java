package de.gurkenwerfer.dougdougclientclient.classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/dougdougclientclient.json");

    private Map<String, Boolean> moduleEnabledMap;

    public ModConfig() {
        this.moduleEnabledMap = new HashMap<>();
    }

    public static ModConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ModConfig();
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Boolean> getModuleEnabledMap() {
        return moduleEnabledMap;
    }

    public boolean isModuleEnabled(String moduleName) {
        return moduleEnabledMap.getOrDefault(moduleName, false);
    }

    public void setModuleEnabled(String moduleName) {
        moduleEnabledMap.put(moduleName, true);
        save();
    }

    public void setModuleDisabled(String moduleName) {
        moduleEnabledMap.put(moduleName, false);
        save();
    }
}