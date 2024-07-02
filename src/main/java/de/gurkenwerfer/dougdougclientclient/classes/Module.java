package de.gurkenwerfer.dougdougclientclient.classes;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

public interface Module {
    void initialize();
    void terminate();
    boolean isEnabled();
    void setEnabled(boolean enabled);
    void buildConfigEntries(ConfigCategory category, ConfigEntryBuilder builder);
}
