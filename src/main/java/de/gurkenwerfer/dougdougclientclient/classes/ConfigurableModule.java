package de.gurkenwerfer.dougdougclientclient.classes;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

public interface ConfigurableModule {
    void buildConfigEntries(ConfigCategory category, ConfigEntryBuilder builder);
}
