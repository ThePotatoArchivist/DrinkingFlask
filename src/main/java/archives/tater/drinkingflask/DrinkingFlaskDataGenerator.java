package archives.tater.drinkingflask;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class DrinkingFlaskDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(TagGenerator::new);
		pack.addProvider(ModelGenerator::new);
		pack.addProvider(LangGenerator::new);
	}

	static class TagGenerator extends FabricTagProvider.ItemTagProvider {
		public TagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup arg) {
			getOrCreateTagBuilder(DrinkingFlask.CAN_POUR_INTO_FLASK);
		}
	}

	static class ModelGenerator extends FabricModelProvider {

		public ModelGenerator(FabricDataOutput output) {
			super(output);
		}

		@Override
		public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

		}

		@Override
		public void generateItemModels(ItemModelGenerator itemModelGenerator) {
			itemModelGenerator.register(DrinkingFlask.DRINKING_FLASK, Models.GENERATED);
			itemModelGenerator.register(DrinkingFlask.PHANTOM_DRINKING_FLASK, Models.GENERATED);
		}
	}

	static class LangGenerator extends FabricLanguageProvider {

		protected LangGenerator(FabricDataOutput dataOutput) {
			super(dataOutput);
		}

		@Override
		public void generateTranslations(TranslationBuilder translationBuilder) {
			translationBuilder.add(DrinkingFlask.DRINKING_FLASK, "Drinking Flask");
			translationBuilder.add(DrinkingFlask.PHANTOM_DRINKING_FLASK, "Phantom Drinking Flask");
		}
	}
}
