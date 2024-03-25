package archives.tater.drinkingflask;

import com.nhoryzon.mc.farmersdelight.registry.ItemsRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DrinkingFlaskDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(TagGenerator::new);
		pack.addProvider(ModelGenerator::new);
		pack.addProvider(LangGenerator::new);
		pack.addProvider(RecipeGenerator::new);
	}

	static class TagGenerator extends FabricTagProvider.ItemTagProvider {
		public TagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup arg) {
			List<Item> canPourItems = List.of(
					ItemsRegistry.TOMATO_SAUCE.get(),
					ItemsRegistry.BONE_BROTH.get(),
					ItemsRegistry.BAKED_COD_STEW.get(),
					ItemsRegistry.BEEF_STEW.get(),
					ItemsRegistry.FISH_STEW.get(),
					ItemsRegistry.CHICKEN_SOUP.get(),
					ItemsRegistry.NOODLE_SOUP.get(),
					ItemsRegistry.PUMPKIN_SOUP.get(),
					ItemsRegistry.VEGETABLE_SOUP.get(),
					ItemsRegistry.GLOW_BERRY_CUSTARD.get()
			);

			FabricTagProvider<Item>.FabricTagBuilder pourTag = getOrCreateTagBuilder(DrinkingFlask.CAN_POUR_INTO_FLASK);
			canPourItems.forEach(item -> pourTag.addOptional(Registries.ITEM.getId(item)));
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
			translationBuilder.add("item.drinkingflask.drinking_flask.fullness", "%s/%s");
		}
	}

	static class RecipeGenerator extends FabricRecipeProvider {

		public RecipeGenerator(FabricDataOutput output) {
			super(output);
		}

		@Override
		public void generate(Consumer<RecipeJsonProvider> exporter) {
			ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, DrinkingFlask.DRINKING_FLASK)
					.pattern(" % ")
					.pattern("#$#")
					.pattern(" # ")
					.input('%', Items.IRON_INGOT)
					.input('$', Items.HONEYCOMB)
					.input('#', Ingredient.ofItems(Items.LEATHER, Items.RABBIT_HIDE))
					.criterion(hasItem(Items.POTION), conditionsFromItem(Items.POTION))
					.offerTo(exporter);

			ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, DrinkingFlask.PHANTOM_DRINKING_FLASK)
					.pattern(" % ")
					.pattern("#$#")
					.pattern(" # ")
					.input('%', Items.IRON_INGOT)
					.input('$', Items.HONEYCOMB)
					.input('#', Items.PHANTOM_MEMBRANE)
					.criterion(hasItem(DrinkingFlask.DRINKING_FLASK), conditionsFromItem(DrinkingFlask.DRINKING_FLASK))
					.offerTo(exporter);
		}
	}
}
