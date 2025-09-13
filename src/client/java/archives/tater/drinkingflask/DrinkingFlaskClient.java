package archives.tater.drinkingflask;

import archives.tater.drinkingflask.client.gui.FlaskTooltipComponent;
import archives.tater.drinkingflask.item.DrinkingFlaskItem;
import archives.tater.drinkingflask.component.FlaskContentsComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class DrinkingFlaskClient implements ClientModInitializer {
    public static final String FULLNESS_TRANSLATION = "item.drinkingflask.drinking_flask.fullness";

    @Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		TooltipComponentCallback.EVENT.register(data ->
				data instanceof FlaskContentsComponent flaskData ? new FlaskTooltipComponent(flaskData) : null
		);
        ItemTooltipCallback.EVENT.register((stack, context, type, tooltip) -> {
            int maxSize = DrinkingFlaskItem.getCapacity(stack);
            if (maxSize <= 0) return;
            tooltip.add(Text.translatable(FULLNESS_TRANSLATION, DrinkingFlaskItem.getFlaskSize(stack), maxSize).formatted(Formatting.GRAY));
        });
	}
}
