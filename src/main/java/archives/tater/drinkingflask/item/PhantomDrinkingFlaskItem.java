package archives.tater.drinkingflask.item;

import java.util.Optional;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PhantomDrinkingFlaskItem extends DrinkingFlaskItem {
    public PhantomDrinkingFlaskItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        var contents = getContents(stack);
        return contents.isEmpty() ? Optional.empty() : Optional.of(contents);
    }
}
