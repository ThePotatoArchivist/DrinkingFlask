package archives.tater.drinkingflask.registry;

import archives.tater.drinkingflask.DrinkingFlask;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class DrinkingFlaskItemTags {

    private static TagKey<Item> of(Identifier id) {
        return TagKey.create(Registries.ITEM, id);
    }

    private static TagKey<Item> of(String path) {
        return of(DrinkingFlask.id(path));
    }

    public static final TagKey<Item> FLASK_MATERIAL = of("flask_material");
    public static final TagKey<Item> CAN_POUR_INTO_FLASK = of("can_pour_into_flask");
    public static final TagKey<Item> DOUBLE_SIZE = of("double_size");
}
