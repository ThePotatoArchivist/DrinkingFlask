package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.DrinkingFlask;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Util;

import java.util.concurrent.CompletableFuture;

public class LangGenerator extends FabricLanguageProvider {

    public LangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(DrinkingFlask.DRINKING_FLASK, "Drinking Flask");
        translationBuilder.add(DrinkingFlask.PHANTOM_DRINKING_FLASK, "Phantom Drinking Flask");
        translationBuilder.add("item.drinkingflask.drinking_flask.fullness", "%s/%s");
        translationBuilder.add(Util.createTranslationKey("tag.item", DrinkingFlask.CAN_POUR_INTO_FLASK.id()), "Drinking Flask Drinks");
    }
}
