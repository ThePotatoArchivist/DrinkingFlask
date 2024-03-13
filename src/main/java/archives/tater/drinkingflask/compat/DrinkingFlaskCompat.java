package archives.tater.drinkingflask.compat;

import com.nhoryzon.mc.farmersdelight.item.ConsumableItem;
import com.simibubi.create.AllItems;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DrinkingFlaskCompat {
    /**
     * Mixin this method for compatibility!
     */
    public static boolean applyEffect(ItemStack stack, World world, LivingEntity user) {
        // Create Builder's Tea
        if (FabricLoader.getInstance().isModLoaded("create") && stack.isOf(AllItems.BUILDERS_TEA.asItem())) {
            PlayerEntity playerentity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
            if (playerentity instanceof ServerPlayerEntity)
                Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) playerentity, stack);

            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 3 * 60 * 20, 0, false, false, false));

            if (playerentity != null) {
                playerentity.incrementStat(Stats.USED.getOrCreateStat(AllItems.BUILDERS_TEA.asItem()));
                playerentity.getHungerManager().add(1, .6F);
                if (!playerentity.getAbilities().creativeMode)
                    stack.decrement(1);
            }
            return true;
        }

        if (FabricLoader.getInstance().isModLoaded("farmersdelight") && stack.getItem() instanceof ConsumableItem consumableItem) {
            consumableItem.affectConsumer(stack, world, user);

            if (stack.isFood()) {
                user.eatFood(world, stack);
            } else if (user instanceof PlayerEntity player) {
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    Criteria.CONSUME_ITEM.trigger(serverPlayer, stack);
                }

                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
            }
            return true;
        }

        return false;
    }

    /**
     * Mixin this method for compatibility!
     */
    public static @Nullable ItemStack getLeftover(ItemStack stack) {
        if (FabricLoader.getInstance().isModLoaded("create") && stack.isOf(AllItems.BUILDERS_TEA.asItem())) {
            return Items.GLASS_BOTTLE.getDefaultStack();
        }
        // Farmer's Delight Milk Bottle has a Recipe Remainder
        return null;
    }
}
