package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.DrinkingFlask;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        var farmersDelightStews = Stream.of(
                "tomato_sauce",
                "bone_broth",
                "baked_cod_stew",
                "beef_stew",
                "fish_stew",
                "chicken_soup",
                "noodle_soup",
                "pumpkin_soup",
                "vegetable_soup"
        ).map(path -> new Identifier("farmersdelight", path)).toList();

        Item[] vanillaStews = {
                Items.MUSHROOM_STEW,
                Items.RABBIT_STEW,
                Items.BEETROOT_SOUP,
                Items.SUSPICIOUS_STEW
        };

        var pourTag = getOrCreateTagBuilder(DrinkingFlask.CAN_POUR_INTO_FLASK)
                .add(Items.POTION, Items.MILK_BUCKET, Items.HONEY_BOTTLE)
                .add(vanillaStews)
                .addOptional(new Identifier("farmersdelight", "glow_berry_custard"));
        farmersDelightStews.forEach(pourTag::addOptional);

        getOrCreateTagBuilder(DrinkingFlask.BOTTLE_REMAINDER)
                .add(Items.POTION)
                .addOptional(new Identifier("farmersdelight", "glow_berry_custard"))
                .addOptional(new Identifier("create", "builders_tea"));

        var bowlTag = getOrCreateTagBuilder(DrinkingFlask.BOWL_REMAINDER)
                .add(vanillaStews);
        farmersDelightStews.forEach(bowlTag::addOptional);

        getOrCreateTagBuilder(DrinkingFlask.BUCKET_REMAINDER);
    }
}
