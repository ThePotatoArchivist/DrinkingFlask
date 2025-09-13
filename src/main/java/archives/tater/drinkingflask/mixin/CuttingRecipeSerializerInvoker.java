package archives.tater.drinkingflask.mixin;

import net.minecraft.recipe.CuttingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CuttingRecipe.Serializer.class)
public interface CuttingRecipeSerializerInvoker {
    @Invoker("<init>")
    static <T extends CuttingRecipe> CuttingRecipe.Serializer<T> newSerializer(CuttingRecipe.RecipeFactory<T> recipeFactory) {
        throw new AssertionError();
    }
}
