package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.registry.DrinkingFlaskItemTags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ItemTagGenerator extends FabricTagsProvider.ItemTagsProvider {
    public ItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    private static List<Identifier> ids(String name, String... paths) {
        return Stream.of(paths).map(path -> Identifier.fromNamespaceAndPath(name, path)).toList();
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        builder(DrinkingFlaskItemTags.FLASK_MATERIAL)
                .forceAddTag(ConventionalItemTags.LEATHERS)
                .add(ItemIds.RABBIT_HIDE);

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

        builder(DrinkingFlaskItemTags.CAN_POUR_INTO_FLASK)
                .add(ItemIds.OMINOUS_BOTTLE)
                .add(ItemIds.MILK_BUCKET)
                .add(ItemIds.POTION)
                .add(ItemIds.HONEY_BOTTLE)
                .add(ItemIds.MUSHROOM_STEW)
                .add(ItemIds.RABBIT_STEW)
                .add(ItemIds.BEETROOT_SOUP)
                .add(ItemIds.SUSPICIOUS_STEW);
        var canPour = getOrCreateRawBuilder(DrinkingFlaskItemTags.CAN_POUR_INTO_FLASK);
        farmersDelightStews.forEach(canPour::addOptionalElement);
        farmersDelightDrinks.forEach(canPour::addOptionalElement);

        builder(DrinkingFlaskItemTags.DOUBLE_SIZE)
                .add(ItemIds.POTION)
                .add(ItemIds.MILK_BUCKET);
        getOrCreateRawBuilder(DrinkingFlaskItemTags.DOUBLE_SIZE)
                .addOptionalElement(Identifier.fromNamespaceAndPath("create", "builders_tea"));
    }
}
