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
            boolean isEnabled = ModConfig.load().isModuleEnabled(moduleName);
            String module = moduleName; // Capture moduleName in a local variable

            //create actual button
            builder.getOrCreateCategory(Text.of("Modules"))
                    .addEntry(builder.entryBuilder().startBooleanToggle(Text.of(module), isEnabled)
                            .setDefaultValue(false)
                            .setSaveConsumer(enabled -> {
                                if (enabled) {
                                    config.setModuleEnabled(module);
                                } else {
                                    config.setModuleDisabled(module);
                                }
                                config.save(); // Save config after modifying module state
                            })
                            .build());
        }

        builder.setSavingRunnable(config::save);

        return builder.build();
    }
}
