package archives.tater.drinkingflask;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DrinkingFlaskItem extends Item {
    public DrinkingFlaskItem(Settings settings) {
        super(settings);
    }

    public static String CONTENTS_KEY = "Contents";

    public static boolean canInsert(ItemStack itemStack) {
        return !(itemStack.getItem() instanceof DrinkingFlaskItem) && (
                itemStack.getUseAction() == UseAction.DRINK ||
                itemStack.getItem() instanceof StewItem ||
                itemStack.getItem() instanceof SuspiciousStewItem ||
                itemStack.isIn(DrinkingFlask.CAN_POUR_INTO_FLASK)
        );
    }

    public static @Nullable NbtList getContents(ItemStack itemStack) {
        NbtCompound nbt = itemStack.getNbt();
        if (nbt == null) return null;
        return nbt.getList(CONTENTS_KEY, NbtElement.COMPOUND_TYPE);
    }

    public static void setContents(ItemStack itemStack, NbtList contents) {
        itemStack.setSubNbt(CONTENTS_KEY, contents);
    }

    /**
     * Mixin this method for compatibility!
     */
    public static void applyEffect(ItemStack stack, World world, LivingEntity user) {
        // Honey bottles need a special case because they add a glass bottle to the inventory
        if (stack.isOf(Items.HONEY_BOTTLE)) {
            user.eatFood(world, stack);
            if (!world.isClient) {
                user.removeStatusEffect(StatusEffects.POISON);
            }
            return;
        }
        stack.finishUsing(world, user);
    }

    /**
     * Mixin this method for compatibility!
     */
    public static ItemStack getLeftover(ItemStack stack) {
        if (stack.getItem().hasRecipeRemainder())
            return stack.getRecipeRemainder();
        if (stack.isOf(Items.POTION))
            return Items.GLASS_BOTTLE.getDefaultStack();
        if (stack.getItem() instanceof StewItem || stack.getItem() instanceof SuspiciousStewItem)
            return Items.BOWL.getDefaultStack();
        return ItemStack.EMPTY;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Hand otherHand = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
        ItemStack otherStack = user.getStackInHand(otherHand);

        ItemStack stack = user.getStackInHand(hand);
        NbtList contents = getContents(stack);
        if (!canInsert(otherStack) || contents != null && contents.size() >= 8) {
            if (contents == null || contents.isEmpty()) {
                return TypedActionResult.fail(stack);
            }
            return ItemUsage.consumeHeldItem(world, user, hand);
        }


        if (contents == null) {
            contents = new NbtList();
        }
        contents.add(otherStack.copyWithCount(1).writeNbt(new NbtCompound()));
        setContents(stack, contents);
        if (otherStack.getCount() > 1) {
            otherStack.decrement(1);
            user.giveItemStack(getLeftover(otherStack));
        } else {
            user.setStackInHand(otherHand, getLeftover(otherStack));
        }
        return TypedActionResult.success(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        super.finishUsing(stack, world, user);
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        if (!world.isClient) {
            @Nullable NbtList contents = getContents(stack);
            if (contents == null || contents.isEmpty()) return stack;
            int index = world.random.nextInt(contents.size());
            applyEffect(
                    ItemStack.fromNbt((NbtCompound) contents.get(index)),
                    world,
                    user);
            contents.remove(index);
        }
        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }
}
