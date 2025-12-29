package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.registry.DrinkingFlaskItemTags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {

    public static final String FARMERS_DELIGHT = "farmersdelight";
    public static final String BREWIN_AND_CHEWIN = "brewinandchewin";

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

        var farmersDelightStews = ids(FARMERS_DELIGHT,
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
        var brewinAndChewinStews = ids(BREWIN_AND_CHEWIN,
        "creamy_onion_soup",
                "fiery_fondue"
        );
        var nethersDelightStews = ids("mynethersdelight",
                "spicy_hoglin_stew",
                "spicy_noodle_soup"
        );

        var brewinAndChewinJams = ids(BREWIN_AND_CHEWIN,
                "sweet_berry_jam",
                "glow_berry_marmalade",
                "apple_jelly"
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
                .forceAddTag(DrinkingFlaskItemTags.TANKARD_REMAINDER)
                .add(Items.OMINOUS_BOTTLE);

        var bottleTag = getOrCreateTagBuilder(DrinkingFlaskItemTags.BOTTLE_REMAINDER)
                .add(Items.POTION, Items.HONEY_BOTTLE)
                .addOptional(buildersTea)
                .addOptional(Identifier.of(FARMERS_DELIGHT, "glow_berry_custard"))
                .forceAddTag(TagKey.of(RegistryKeys.ITEM, Identifier.of(FARMERS_DELIGHT, "drinks")))
                .addOptional(Identifier.of("enderscape", "drift_jelly_bottle"));
        brewinAndChewinJams.forEach(bottleTag::addOptional);

        var bowlTag = getOrCreateTagBuilder(DrinkingFlaskItemTags.BOWL_REMAINDER)
                .add(vanillaStews);
        farmersDelightStews.forEach(bowlTag::addOptional);
        nethersDelightStews.forEach(bowlTag::addOptional);
        brewinAndChewinStews.forEach(bowlTag::addOptional);

        getOrCreateTagBuilder(DrinkingFlaskItemTags.BUCKET_REMAINDER)
                .add(Items.MILK_BUCKET);

        getOrCreateTagBuilder(DrinkingFlaskItemTags.TANKARD_REMAINDER)
                .forceAddTag(TagKey.of(RegistryKeys.ITEM, Identifier.of(BREWIN_AND_CHEWIN, "fermented_drinks")));

        getOrCreateTagBuilder(DrinkingFlaskItemTags.DOUBLE_SIZE)
                .add(Items.POTION, Items.MILK_BUCKET)
                .addOptional(buildersTea);
    }
}
