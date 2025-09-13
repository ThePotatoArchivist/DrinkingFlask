package archives.tater.drinkingflask;

import archives.tater.drinkingflask.client.gui.FlaskTooltipComponent;
import archives.tater.drinkingflask.item.DrinkingFlaskItem;
import archives.tater.drinkingflask.item.FlaskContentsComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

public class DrinkingFlaskClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		TooltipComponentCallback.EVENT.register(data ->
				data instanceof FlaskContentsComponent flaskData ? new FlaskTooltipComponent(flaskData) : null
		);
        ItemTooltipCallback.EVENT.register(DrinkingFlaskItem::appendTooltip);
	}
}
