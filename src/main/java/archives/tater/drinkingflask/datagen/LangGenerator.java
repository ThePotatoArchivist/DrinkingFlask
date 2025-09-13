package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.DrinkingFlask;
import archives.tater.drinkingflask.item.DrinkingFlaskItem;
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
        translationBuilder.add(DrinkingFlask.DRINKING_FLASK, "Drinking Flask");
        translationBuilder.add(DrinkingFlask.PHANTOM_DRINKING_FLASK, "Phantom Drinking Flask");
        translationBuilder.add(DrinkingFlaskItem.FULLNESS_TRANSLATION, "%s/%s");
        translationBuilder.add(DrinkingFlask.CAN_POUR_INTO_FLASK, "Can Pour into Flask");
        translationBuilder.add(DrinkingFlask.BOTTLE_REMAINDER, "Bottle Remainder");
        translationBuilder.add(DrinkingFlask.BOWL_REMAINDER, "Bowl Remainder");
        translationBuilder.add(DrinkingFlask.BUCKET_REMAINDER, "Bucket Remainder");
        translationBuilder.add(DrinkingFlask.DOUBLE_SIZE, "Double Size in Flask");
        translationBuilder.add(DrinkingFlask.FLASK_MATERIAL, "Drinking Flask Material");
    }
}
