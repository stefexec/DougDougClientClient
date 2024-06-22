package de.gurkenwerfer.dougdougclientclient;

import com.terraformersmc.modmenu.api.ModMenuApi;
import de.gurkenwerfer.dougdougclientclient.classes.Keybinds;
import de.gurkenwerfer.dougdougclientclient.classes.ModConfig;
import de.gurkenwerfer.dougdougclientclient.classes.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dougdougclientclient implements ClientModInitializer, ModMenuApi {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("dougdougclientclient");
	public static ModConfig config;

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Gurked world!");
		config = ModConfig.load();
		Keybinds.register();

		ModuleManager.initializeModules();
		System.out.println("DougDougClientClient has been initialized on the client side!");
	}

}