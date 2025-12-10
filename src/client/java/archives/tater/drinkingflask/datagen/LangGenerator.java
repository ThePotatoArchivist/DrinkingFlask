package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.DrinkingFlaskClient;
import archives.tater.drinkingflask.registry.DrinkingFlaskItemTags;
import archives.tater.drinkingflask.registry.DrinkingFlaskItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class LangGenerator extends FabricLanguageProvider {

    public LangGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(DrinkingFlaskItems.DRINKING_FLASK, "Drinking Flask");
        translationBuilder.add(DrinkingFlaskItems.PHANTOM_DRINKING_FLASK, "Phantom Drinking Flask");
        translationBuilder.add(DrinkingFlaskClient.FULLNESS_TRANSLATION, "%s/%s");
        translationBuilder.add(DrinkingFlaskItemTags.CAN_POUR_INTO_FLASK, "Can Pour into Flask");
        translationBuilder.add(DrinkingFlaskItemTags.DOUBLE_SIZE, "Double Size in Flask");
        translationBuilder.add(DrinkingFlaskItemTags.FLASK_MATERIAL, "Drinking Flask Material");
    }
}
