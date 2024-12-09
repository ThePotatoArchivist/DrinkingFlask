package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.DrinkingFlask;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class LangGenerator extends FabricLanguageProvider {

    public LangGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(DrinkingFlask.DRINKING_FLASK, "Drinking Flask");
        translationBuilder.add(DrinkingFlask.PHANTOM_DRINKING_FLASK, "Phantom Drinking Flask");
        translationBuilder.add("item.drinkingflask.drinking_flask.fullness", "%s/%s");
    }
}
