package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.registry.DrinkingFlaskItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    private static List<Identifier> ids(String name, String... paths) {
        return Stream.of(paths).map(path -> Identifier.of(name, path)).toList();
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(DrinkingFlaskItemTags.FLASK_MATERIAL)
                .forceAddTag(ConventionalItemTags.LEATHERS)
                .add(Items.RABBIT_HIDE);

        var farmersDelightStews = ids("farmersdelight",
                "tomato_sauce",
                "bone_broth",
                "baked_cod_stew",
                "beef_stew",
                "fish_stew",
                "chicken_soup",
                "noodle_soup",
                "pumpkin_soup",
                "vegetable_soup"
        );

        var farmersDelightDrinks = ids("farmersdelight",
                "apple_cider",
                "melon_juice",
                "hot_cocoa",
                "milk_bottle",
                "glow_berry_custard"
        );

        Item[] vanillaStews = {
                Items.MUSHROOM_STEW,
                Items.RABBIT_STEW,
                Items.BEETROOT_SOUP,
                Items.SUSPICIOUS_STEW
        };

        Identifier buildersTea = Identifier.of("create", "builders_tea");

        getOrCreateTagBuilder(DrinkingFlaskItemTags.CAN_POUR_INTO_FLASK)
                .forceAddTag(DrinkingFlaskItemTags.BOTTLE_REMAINDER)
                .forceAddTag(DrinkingFlaskItemTags.BOWL_REMAINDER)
                .forceAddTag(DrinkingFlaskItemTags.BUCKET_REMAINDER)
                .add(Items.OMINOUS_BOTTLE);

        var bottleTag = getOrCreateTagBuilder(DrinkingFlaskItemTags.BOTTLE_REMAINDER)
                .add(Items.POTION, Items.HONEY_BOTTLE)
                .addOptional(buildersTea);
        farmersDelightDrinks.forEach(bottleTag::addOptional);

        var bowlTag = getOrCreateTagBuilder(DrinkingFlaskItemTags.BOWL_REMAINDER)
                .add(vanillaStews);
        farmersDelightStews.forEach(bowlTag::addOptional);

        getOrCreateTagBuilder(DrinkingFlaskItemTags.BUCKET_REMAINDER)
                .add(Items.MILK_BUCKET);

        getOrCreateTagBuilder(DrinkingFlaskItemTags.DOUBLE_SIZE)
                .add(Items.POTION, Items.MILK_BUCKET)
                .addOptional(buildersTea);
    }
}
