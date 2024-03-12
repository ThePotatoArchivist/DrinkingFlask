package archives.tater.drinkingflask.mixin.client;

import archives.tater.drinkingflask.client.FlaskTooltipData;
import archives.tater.drinkingflask.client.gui.FlaskTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TooltipComponent.class)
public interface TooltipComponentMixin {
	@Inject(at = @At("HEAD"), method = "of(Lnet/minecraft/client/item/TooltipData;)Lnet/minecraft/client/gui/tooltip/TooltipComponent;", cancellable = true)
	private static void handleFlaskTooltip(TooltipData data, CallbackInfoReturnable<TooltipComponent> cir) {
		if (data instanceof FlaskTooltipData flaskData) {
			cir.setReturnValue(new FlaskTooltipComponent(flaskData));
		}
	}
}
