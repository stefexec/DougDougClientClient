package de.gurkenwerfer.dougdougclientclient;

import com.terraformersmc.modmenu.api.ModMenuApi;
import de.gurkenwerfer.dougdougclientclient.classes.Keybinds;
import de.gurkenwerfer.dougdougclientclient.classes.ModConfig;
import de.gurkenwerfer.dougdougclientclient.classes.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dougdougclientclient implements ClientModInitializer, ModMenuApi {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("dougdougclientclient");
	public static ModConfig config;

	private boolean hasJoinedGame = false;

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Gurked world!");
		config = ModConfig.load();
		ModuleManager.initializeModules();

		Keybinds.register();
		LOGGER.info("DougDougClientClient has been initialized on the client side!");

		// Register the event listener for game join
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.world != null && client.player != null) {
				if (!hasJoinedGame) {
					ModuleManager.onGameJoin();
					hasJoinedGame = true;
				}
			} else {
				if (hasJoinedGame) {
					hasJoinedGame = false;
					LOGGER.info("Player left the game, flag reset.");
				}
			}
		});
	}
}
