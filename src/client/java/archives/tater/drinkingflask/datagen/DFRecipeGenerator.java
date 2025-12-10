package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.registry.DrinkingFlaskItemTags;
import archives.tater.drinkingflask.registry.DrinkingFlaskItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.concurrent.CompletableFuture;

public class DFRecipeGenerator extends RecipeProvider {

    protected DFRecipeGenerator(HolderLookup.Provider registries, RecipeOutput exporter) {
        super(registries, exporter);
    }

    @Override
    public void buildRecipes() {
        var itemLookup = registries.lookupOrThrow(Registries.ITEM);

        shaped(RecipeCategory.TOOLS, DrinkingFlaskItems.DRINKING_FLASK)
                .pattern(" % ")
                .pattern("#$#")
                .pattern(" # ")
                .input('%', ConventionalItemTags.IRON_INGOTS)
                .input('$', Items.HONEYCOMB)
                .input('#', Ingredient.of(itemLookup.getOrThrow(DrinkingFlaskItemTags.FLASK_MATERIAL)))
                .criterion("has_material", has(DrinkingFlaskItemTags.FLASK_MATERIAL))
                .offerTo(output);

        shaped(RecipeCategory.TOOLS, DrinkingFlaskItems.PHANTOM_DRINKING_FLASK)
                .pattern(" % ")
                .pattern("#$#")
                .pattern(" # ")
                .input('%', ConventionalItemTags.IRON_INGOTS)
                .input('$', Items.HONEYCOMB)
                .input('#', Items.PHANTOM_MEMBRANE)
                .criterion(getHasName(DrinkingFlaskItems.DRINKING_FLASK), has(DrinkingFlaskItems.DRINKING_FLASK))
                .offerTo(output);
    }

    public static class Provider extends FabricRecipeProvider {

        public Provider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput recipeExporter) {
            return new DFRecipeGenerator(wrapperLookup, recipeExporter);
        }

        @Override
        public String getName() {
            return "Recipes";
        }
    }
}
