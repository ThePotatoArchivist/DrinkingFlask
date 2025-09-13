package archives.tater.drinkingflask.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;

import java.util.Optional;

public class PhantomDrinkingFlaskItem extends DrinkingFlaskItem {
    public PhantomDrinkingFlaskItem(Settings settings) {
        super(settings);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        var contents = getContents(stack);
        return contents.isEmpty() ? Optional.empty() : Optional.of(contents);
    }
}
