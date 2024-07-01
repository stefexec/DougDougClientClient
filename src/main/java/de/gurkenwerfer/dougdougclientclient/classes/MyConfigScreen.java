package de.gurkenwerfer.dougdougclientclient.classes;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MyConfigScreen {
    public static Screen getConfigScreen(Screen parent) {
        ModConfig config = ModConfig.load(); // Load the configuration instance

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.dougdougclientclient.config"));

        for (String moduleName : ModuleManager.getModuleNames()) {
            Module module = ModuleManager.getModule(moduleName);

            if (module instanceof ConfigurableModule) {
                ConfigCategory category = builder.getOrCreateCategory(Text.of(moduleName));
                ConfigEntryBuilder entryBuilder = builder.entryBuilder();

                // Build configuration entries for the module
                ((ConfigurableModule) module).buildConfigEntries(category, entryBuilder);
            }
        }

        builder.setSavingRunnable(config::save);

        return builder.build();
    }
}
