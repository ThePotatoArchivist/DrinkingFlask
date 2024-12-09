package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.DrinkingFlask;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
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
        return Stream.of(paths).map(path -> new Identifier(name, path)).toList();
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
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

        Identifier buildersTea = new Identifier("create", "builders_tea");

        var pourTag = getOrCreateTagBuilder(DrinkingFlask.CAN_POUR_INTO_FLASK)
                .add(Items.POTION, Items.MILK_BUCKET, Items.HONEY_BOTTLE)
                .add(vanillaStews)
                .addOptional(buildersTea);
        farmersDelightStews.forEach(pourTag::addOptional);
        farmersDelightDrinks.forEach(pourTag::addOptional);

        var bottleTag = getOrCreateTagBuilder(DrinkingFlask.BOTTLE_REMAINDER)
                .add(Items.POTION)
                .addOptional(buildersTea);
        farmersDelightDrinks.forEach(bottleTag::addOptional);

        var bowlTag = getOrCreateTagBuilder(DrinkingFlask.BOWL_REMAINDER)
                .add(vanillaStews);
        farmersDelightStews.forEach(bowlTag::addOptional);

        getOrCreateTagBuilder(DrinkingFlask.BUCKET_REMAINDER);

        var doubleTag = getOrCreateTagBuilder(DrinkingFlask.DOUBLE_SIZE)
                .add(Items.POTION, Items.MILK_BUCKET)
                .addOptional(buildersTea)
                .addOptional(new Identifier("farmersdelight", "apple_cider")); // regen
    }
}
