package archives.tater.drinkingflask;

import archives.tater.drinkingflask.datagen.LangGenerator;
import archives.tater.drinkingflask.datagen.ModelGenerator;
import archives.tater.drinkingflask.datagen.DFRecipeGenerator;
import archives.tater.drinkingflask.datagen.ItemTagGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DrinkingFlaskDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(ModelGenerator::new);
		pack.addProvider(LangGenerator::new);
		pack.addProvider(DFRecipeGenerator.Provider::new);
	}
}
