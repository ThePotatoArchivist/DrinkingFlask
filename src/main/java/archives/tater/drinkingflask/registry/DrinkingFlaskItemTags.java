package archives.tater.drinkingflask.registry;

import archives.tater.drinkingflask.DrinkingFlask;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class DrinkingFlaskItemTags {

    private static TagKey<Item> of(Identifier id) {
        return TagKey.of(RegistryKeys.ITEM, id);
    }

    private static TagKey<Item> of(String path) {
        return of(DrinkingFlask.id(path));
    }

    public static final TagKey<Item> FLASK_MATERIAL = of("flask_material");
    public static final TagKey<Item> CAN_POUR_INTO_FLASK = of("can_pour_into_flask");
    public static final TagKey<Item> BOTTLE_REMAINDER = of("remainder/glass_bottle");
    public static final TagKey<Item> BOWL_REMAINDER = of("remainder/bowl");
    public static final TagKey<Item> BUCKET_REMAINDER = of("remainder/bucket");
    public static final TagKey<Item> DOUBLE_SIZE = of("double_size");
}
