package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.item.DrinkingFlaskItem;
import archives.tater.drinkingflask.registry.DrinkingFlaskItemTags;
import archives.tater.drinkingflask.registry.DrinkingFlaskItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class LangGenerator extends FabricLanguageProvider {

    public LangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(DrinkingFlaskItems.DRINKING_FLASK, "Drinking Flask");
        translationBuilder.add(DrinkingFlaskItems.PHANTOM_DRINKING_FLASK, "Phantom Drinking Flask");
        translationBuilder.add(DrinkingFlaskItem.FULLNESS_TRANSLATION, "%s/%s");
        translationBuilder.add(DrinkingFlaskItemTags.CAN_POUR_INTO_FLASK, "Can Pour into Flask");
        translationBuilder.add(DrinkingFlaskItemTags.BOTTLE_REMAINDER, "Bottle Remainder");
        translationBuilder.add(DrinkingFlaskItemTags.BOWL_REMAINDER, "Bowl Remainder");
        translationBuilder.add(DrinkingFlaskItemTags.BUCKET_REMAINDER, "Bucket Remainder");
        translationBuilder.add(DrinkingFlaskItemTags.DOUBLE_SIZE, "Double Size in Flask");
        translationBuilder.add(DrinkingFlaskItemTags.FLASK_MATERIAL, "Drinking Flask Material");
    }
}
