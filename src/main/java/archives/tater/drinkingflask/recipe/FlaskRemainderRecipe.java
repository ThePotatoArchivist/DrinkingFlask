package archives.tater.drinkingflask.recipe;

import archives.tater.drinkingflask.registry.DrinkingFlaskRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.world.World;

public class FlaskRemainderRecipe extends CuttingRecipe {
    public FlaskRemainderRecipe(String group, Ingredient ingredient, ItemStack result) {
        super(DrinkingFlaskRecipes.REMAINDER_RECIPE_TYPE, DrinkingFlaskRecipes.REMAINDER_RECIPE_SERIALIZER, group, ingredient, result);
    }

    public FlaskRemainderRecipe(Ingredient ingredient, ItemStack result) {
        this("", ingredient, result);
    }

    @Override
    public boolean matches(SingleStackRecipeInput input, World world) {
        return ingredient.test(input.item());
    }
}
