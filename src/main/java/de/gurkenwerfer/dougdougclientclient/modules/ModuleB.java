package de.gurkenwerfer.dougdougclientclient.modules;

import de.gurkenwerfer.dougdougclientclient.classes.Module;

public class ModuleB implements Module {
    private boolean enabled = true; // Default to enabled

    @Override
    public void initialize() {
        System.out.println("ModuleB initialized!");
        // Your module-specific initialization code here
    }

    @Override
    public void terminate() {
        System.out.println("ModuleB terminated!");
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

}
