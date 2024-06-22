package de.gurkenwerfer.dougdougclientclient.classes;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MyConfigScreen {
    public static Screen getConfigScreen(Screen parent) {
        ModConfig config = ModConfig.load();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.dougdougclientclient.config"));

        for (String moduleName : ModuleManager.getModuleNames()) {
            boolean isEnabled = config.isModuleEnabled(moduleName);
            System.out.println("Module " + moduleName + " is enabled: " + isEnabled);

            // Create actual button
            builder.getOrCreateCategory(Text.of("Modules"))
                    .addEntry(builder.entryBuilder().startBooleanToggle(Text.of(moduleName), isEnabled)
                            .setDefaultValue(false)
                            .setSaveConsumer(enabled -> {
                                System.out.println("Setting module " + moduleName + " to " + enabled);
                                if (enabled) {
                                    config.setModuleEnabled(moduleName);
                                    ModuleManager.enableModule(moduleName);
                                } else {
                                    config.setModuleDisabled(moduleName);
                                    ModuleManager.disableModule(moduleName);
                                }
                                config.save();
                            })
                            .build());
        }

        builder.setSavingRunnable(config::save);

        return builder.build();
    }
}