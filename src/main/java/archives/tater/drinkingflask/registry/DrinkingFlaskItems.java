package archives.tater.drinkingflask.registry;

import archives.tater.drinkingflask.DrinkingFlask;
import archives.tater.drinkingflask.item.DrinkingFlaskItem;
import archives.tater.drinkingflask.item.PhantomDrinkingFlaskItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class DrinkingFlaskItems {
    public static final Item DRINKING_FLASK = register("drinking_flask", DrinkingFlaskItem::new, new Item.Settings()
            .maxCount(1)
            .component(DrinkingFlaskComponents.FLASK_CAPACITY, 16)
    );
    public static final Item PHANTOM_DRINKING_FLASK = register("phantom_drinking_flask", PhantomDrinkingFlaskItem::new, new Item.Settings()
            .maxCount(1)
            .component(DrinkingFlaskComponents.FLASK_CAPACITY, 16)
    );

    private static Item register(Identifier id, Function<Item.Settings, Item> item, Item.Settings settings) {
        return Registry.register(Registries.ITEM, id, item.apply(settings));
    }

    private static Item register(String path, Function<Item.Settings, Item> item, Item.Settings settings) {
        return register(DrinkingFlask.id(path), item, settings);
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(DrinkingFlaskItems.DRINKING_FLASK);
            entries.add(DrinkingFlaskItems.PHANTOM_DRINKING_FLASK);
        });
    }
}
