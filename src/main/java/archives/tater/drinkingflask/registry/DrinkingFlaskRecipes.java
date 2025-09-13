package archives.tater.drinkingflask.registry;

import archives.tater.drinkingflask.DrinkingFlask;
import archives.tater.drinkingflask.mixin.CuttingRecipeSerializerInvoker;
import archives.tater.drinkingflask.recipe.FlaskRemainderRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DrinkingFlaskRecipes {
    public static final RecipeType<FlaskRemainderRecipe> REMAINDER_RECIPE_TYPE = registerRecipeType(DrinkingFlask.id("remainder"));
    public static final RecipeSerializer<FlaskRemainderRecipe> REMAINDER_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, DrinkingFlask.id("remainder"), CuttingRecipeSerializerInvoker.newSerializer(FlaskRemainderRecipe::new));

    public static void init() {

    }

    private static <T extends Recipe<?>> RecipeType<T> registerRecipeType(Identifier id) {
        return Registry.register(Registries.RECIPE_TYPE, id, new RecipeType<T>() {
            @Override
            public String toString() {
                return id.toString();
            }
        });
    }
}
