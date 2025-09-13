package archives.tater.drinkingflask;

import archives.tater.drinkingflask.registry.DrinkingFlaskComponents;
import archives.tater.drinkingflask.registry.DrinkingFlaskItems;
import archives.tater.drinkingflask.registry.DrinkingFlaskRecipes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrinkingFlask implements ModInitializer {
	public static final String MOD_ID = "drinkingflask";

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        DrinkingFlaskRecipes.init();
        DrinkingFlaskComponents.init();
        DrinkingFlaskItems.init();
	}
}
