package archives.tater.drinkingflask;

import archives.tater.drinkingflask.client.FlaskTooltipData;
import archives.tater.drinkingflask.client.gui.FlaskTooltipComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

public class DrinkingFlaskClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		TooltipComponentCallback.EVENT.register(data ->
				data instanceof FlaskTooltipData flaskData ? new FlaskTooltipComponent(flaskData) : null
		);
	}
}
