package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.registry.DrinkingFlaskItemTags;
import archives.tater.drinkingflask.registry.DrinkingFlaskItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class DFRecipeGenerator extends RecipeGenerator {

    protected DFRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
    }

    @Override
    public void generate() {
        var itemLookup = registries.getOrThrow(RegistryKeys.ITEM);

        createShaped(RecipeCategory.TOOLS, DrinkingFlaskItems.DRINKING_FLASK)
                .pattern(" % ")
                .pattern("#$#")
                .pattern(" # ")
                .input('%', ConventionalItemTags.IRON_INGOTS)
                .input('$', Items.HONEYCOMB)
                .input('#', Ingredient.ofTag(itemLookup.getOrThrow(DrinkingFlaskItemTags.FLASK_MATERIAL)))
                .criterion("has_material", conditionsFromTag(DrinkingFlaskItemTags.FLASK_MATERIAL))
                .offerTo(exporter);

        createShaped(RecipeCategory.TOOLS, DrinkingFlaskItems.PHANTOM_DRINKING_FLASK)
                .pattern(" % ")
                .pattern("#$#")
                .pattern(" # ")
                .input('%', ConventionalItemTags.IRON_INGOTS)
                .input('$', Items.HONEYCOMB)
                .input('#', Items.PHANTOM_MEMBRANE)
                .criterion(hasItem(DrinkingFlaskItems.DRINKING_FLASK), conditionsFromItem(DrinkingFlaskItems.DRINKING_FLASK))
                .offerTo(exporter);
    }

    public static class Provider extends FabricRecipeProvider {

        public Provider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
            return new DFRecipeGenerator(wrapperLookup, recipeExporter);
        }

        @Override
        public String getName() {
            return "Recipes";
        }
    }
}
