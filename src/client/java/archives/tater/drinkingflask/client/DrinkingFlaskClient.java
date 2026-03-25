package archives.tater.drinkingflask.client;

import archives.tater.drinkingflask.client.gui.ClientFlaskTooltip;
import archives.tater.drinkingflask.component.FlaskContentsComponent;
import archives.tater.drinkingflask.registry.DrinkingFlaskComponents;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;

public class DrinkingFlaskClient implements ClientModInitializer {
    @Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ClientTooltipComponentCallback.EVENT.register(component ->
				component instanceof FlaskContentsComponent flaskData ? new ClientFlaskTooltip(flaskData) : null
        );
        ItemComponentTooltipProviderRegistry.addFirst(DrinkingFlaskComponents.FLASK_CONTENTS);
	}
}
