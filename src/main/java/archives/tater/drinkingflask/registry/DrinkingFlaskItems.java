package archives.tater.drinkingflask.registry;

import archives.tater.drinkingflask.DrinkingFlask;
import archives.tater.drinkingflask.item.DrinkingFlaskItem;
import archives.tater.drinkingflask.item.PhantomDrinkingFlaskItem;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Function;

public class DrinkingFlaskItems {

    private static Item register(Identifier id, Function<Item.Properties, Item> item, Item.Properties settings) {
        var key = ResourceKey.create(Registries.ITEM, id);
        return Registry.register(BuiltInRegistries.ITEM, key, item.apply(settings.setId(key)));
    }

    private static Item register(String path, Function<Item.Properties, Item> item, Item.Properties settings) {
        return register(DrinkingFlask.id(path), item, settings);
    }

    public static final Item DRINKING_FLASK = register("drinking_flask", DrinkingFlaskItem::new, new Item.Properties()
            .stacksTo(1)
            .component(DrinkingFlaskComponents.FLASK_CAPACITY, 16)
    );
    public static final Item PHANTOM_DRINKING_FLASK = register("phantom_drinking_flask", PhantomDrinkingFlaskItem::new, new Item.Properties()
            .stacksTo(1)
            .component(DrinkingFlaskComponents.FLASK_CAPACITY, 16)
    );

    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
            output.insertAfter(
                    item -> item.getItem() instanceof BundleItem,
                    List.of(DrinkingFlaskItems.DRINKING_FLASK.getDefaultInstance(), DrinkingFlaskItems.PHANTOM_DRINKING_FLASK.getDefaultInstance()),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        });
    }
}
