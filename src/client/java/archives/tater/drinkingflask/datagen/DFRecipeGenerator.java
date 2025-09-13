package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.DrinkingFlask;
import archives.tater.drinkingflask.recipe.FlaskRemainderRecipe;
import archives.tater.drinkingflask.registry.DrinkingFlaskItemTags;
import archives.tater.drinkingflask.registry.DrinkingFlaskItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class DFRecipeGenerator extends FabricRecipeProvider {

    public DFRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    private static void offerRemainderRecipe(RecipeExporter exporter, Ingredient ingredient, Item result) {
        var itemId = Registries.ITEM.getId(result);
        var recipePath = "remainder/" + (itemId.getNamespace().equals(Identifier.DEFAULT_NAMESPACE) ? "" : itemId.getNamespace() + "/") + itemId.getPath();
        exporter.accept(DrinkingFlask.id(recipePath), new FlaskRemainderRecipe(ingredient, result.getDefaultStack()), null);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, DrinkingFlaskItems.DRINKING_FLASK)
                .pattern(" % ")
                .pattern("#$#")
                .pattern(" # ")
                .input('%', ConventionalItemTags.IRON_INGOTS)
                .input('$', Items.HONEYCOMB)
                .input('#', Ingredient.fromTag(DrinkingFlaskItemTags.FLASK_MATERIAL))
                .criterion("has_material", conditionsFromTag(DrinkingFlaskItemTags.FLASK_MATERIAL))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, DrinkingFlaskItems.PHANTOM_DRINKING_FLASK)
                .pattern(" % ")
                .pattern("#$#")
                .pattern(" # ")
                .input('%', ConventionalItemTags.IRON_INGOTS)
                .input('$', Items.HONEYCOMB)
                .input('#', Items.PHANTOM_MEMBRANE)
                .criterion(hasItem(DrinkingFlaskItems.DRINKING_FLASK), conditionsFromItem(DrinkingFlaskItems.DRINKING_FLASK))
                .offerTo(exporter);

        offerRemainderRecipe(exporter, Ingredient.fromTag(DrinkingFlaskItemTags.BOTTLE_REMAINDER), Items.GLASS_BOTTLE);
        offerRemainderRecipe(exporter, Ingredient.fromTag(DrinkingFlaskItemTags.BOWL_REMAINDER), Items.BOWL);
        offerRemainderRecipe(exporter, Ingredient.fromTag(DrinkingFlaskItemTags.BUCKET_REMAINDER), Items.BUCKET);
    }
}
