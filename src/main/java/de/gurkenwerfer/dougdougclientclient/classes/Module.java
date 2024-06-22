package de.gurkenwerfer.dougdougclientclient.classes;

public interface Module {
    void initialize();
    void terminate();
    boolean isEnabled();
    void setEnabled(boolean enabled);
}
