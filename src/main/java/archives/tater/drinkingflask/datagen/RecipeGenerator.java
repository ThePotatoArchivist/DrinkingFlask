package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.DrinkingFlask;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class RecipeGenerator extends FabricRecipeProvider {

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
