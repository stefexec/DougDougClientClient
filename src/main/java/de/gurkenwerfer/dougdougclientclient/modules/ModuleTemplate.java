package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.ConfigurableModule;
import de.gurkenwerfer.dougdougclientclient.classes.Module;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModuleTemplate implements Module, ConfigurableModule {
    private boolean enabled = true; // Default to enabled

    @Override
    public void initialize() {
        System.out.println("ModuleTemplate initialized!");
        // Your module-specific initialization code here
    }

    @Override
    public void terminate() {
        System.out.println("ModuleTemplate terminated!");
        // Your module-specific termination code here
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void buildConfigEntries(ConfigCategory category, ConfigEntryBuilder builder) {
        category.addEntry(builder.startBooleanToggle(Text.of("Enable/Disable The Module"), enabled)
                .setSaveConsumer(value -> enabled = value)
                .build());
    }

}
