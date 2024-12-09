package archives.tater.drinkingflask;

import archives.tater.drinkingflask.api.DrinkingFlaskRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DrinkingFlaskRegistryImpl implements DrinkingFlaskRegistry {
    private static final Map<Item, RemainderProvider> REMAINDERS = new HashMap<>();
    private static final Map<Item, TriConsumer<ItemStack, World, LivingEntity>> ACTIONS = new HashMap<>();

    @Override
    public void registerRemainder(Item input, Item remainder) {
        REMAINDERS.put(input, _input -> remainder.getDefaultStack());
    }

    @Override
    public void registerRemainder(Item input, RemainderProvider remainderProvider) {
        REMAINDERS.put(input, remainderProvider);
    }

    @Override
    public void registerRemainderStack(Item input, ItemStack remainder) {
        REMAINDERS.put(input, _input -> remainder.copy());
    }

    @Override
    public void registerRemainderStack(Item input, ItemRemainderProvider remainderProvider) {
        REMAINDERS.put(input, remainderProvider);
    }

    public static boolean hasRemainder(ItemStack input) {
        return REMAINDERS.containsKey(input.getItem());
    }

    public static @Nullable ItemStack getRemainder(ItemStack input) {
        var provider = REMAINDERS.get(input.getItem());
        if (provider == null) return null;
        return provider.provideRemainder(input);
    }

    public static boolean runAction(ItemStack input, World world, LivingEntity user) {
        var action = ACTIONS.get(input.getItem());
        if (action == null) return false;
        action.accept(input, world, user);
        return true;
    }

    @Override
    public void registerAction(Item input, TriConsumer<ItemStack, World, LivingEntity> action) {
        ACTIONS.put(input, action);
    }
}
