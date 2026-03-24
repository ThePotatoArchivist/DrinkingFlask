package archives.tater.drinkingflask;

import archives.tater.drinkingflask.client.gui.ClientFlaskTooltip;
import archives.tater.drinkingflask.component.FlaskContentsComponent;
import archives.tater.drinkingflask.item.DrinkingFlaskItem;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class DrinkingFlaskClient implements ClientModInitializer {
    public static final String FULLNESS_TRANSLATION = "item.drinkingflask.drinking_flask.fullness";

    @Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ClientTooltipComponentCallback.EVENT.register(component ->
				component instanceof FlaskContentsComponent flaskData ? new ClientFlaskTooltip(flaskData) : null
        );
        ItemTooltipCallback.EVENT.register((stack, context, type, tooltip) -> {
            int maxSize = DrinkingFlaskItem.getCapacity(stack);
            if (maxSize <= 0) return;
            tooltip.add(Component.translatable(FULLNESS_TRANSLATION, DrinkingFlaskItem.getFlaskSize(stack), maxSize).withStyle(ChatFormatting.GRAY));
        });
	}
}
