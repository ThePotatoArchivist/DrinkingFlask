package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.registry.DrinkingFlaskItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    private static List<Identifier> ids(String name, String... paths) {
        return Stream.of(paths).map(path -> Identifier.fromNamespaceAndPath(name, path)).toList();
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        valueLookupBuilder(DrinkingFlaskItemTags.FLASK_MATERIAL)
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

        valueLookupBuilder(DrinkingFlaskItemTags.CAN_POUR_INTO_FLASK)
                .add(
                        Items.OMINOUS_BOTTLE,
                        Items.MILK_BUCKET,
                        Items.POTION,
                        Items.HONEY_BOTTLE,
                        Items.MUSHROOM_STEW,
                        Items.RABBIT_STEW,
                        Items.BEETROOT_SOUP,
                        Items.SUSPICIOUS_STEW
                );
        var canPour = getOrCreateRawBuilder(DrinkingFlaskItemTags.CAN_POUR_INTO_FLASK);
        farmersDelightStews.forEach(canPour::addOptional);
        farmersDelightDrinks.forEach(canPour::addOptional);

        valueLookupBuilder(DrinkingFlaskItemTags.DOUBLE_SIZE)
                .add(Items.POTION, Items.MILK_BUCKET);
        getOrCreateRawBuilder(DrinkingFlaskItemTags.DOUBLE_SIZE)
                .addOptional(Identifier.fromNamespaceAndPath("create", "builders_tea"));
    }
}
