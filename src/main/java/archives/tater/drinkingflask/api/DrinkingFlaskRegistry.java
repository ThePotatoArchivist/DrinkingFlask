package archives.tater.drinkingflask.api;

import archives.tater.drinkingflask.DrinkingFlaskRegistryImpl;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.TriConsumer;

public interface DrinkingFlaskRegistry {
    DrinkingFlaskRegistry INSTANCE = new DrinkingFlaskRegistryImpl();

    void registerRemainder(Item input, Item remainder);
    void registerRemainder(Item input, RemainderProvider remainderProvider);
    void registerRemainderStack(Item input, ItemStack remainder);
    void registerRemainderStack(Item input, ItemRemainderProvider remainderProvider);

    void registerAction(Item input, TriConsumer<ItemStack, World, LivingEntity> action);

    @FunctionalInterface
    interface RemainderProvider {
        ItemStack provideRemainder(ItemStack input);
    }

    @FunctionalInterface
    interface ItemRemainderProvider extends RemainderProvider {
        Item provideRemainderItem(ItemStack input);

        @Override
        default ItemStack provideRemainder(ItemStack input) {
            return provideRemainderItem(input).getDefaultStack();
        }
    }
}
