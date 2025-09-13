package archives.tater.drinkingflask.item;

import archives.tater.drinkingflask.DrinkingFlask;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

import static java.lang.Math.min;

public class DrinkingFlaskItem extends Item {

    private static final int ITEM_BAR_COLOR = MathHelper.packRgb(0.4f, 0.4f, 1.0f);
    public static final String FULLNESS_TRANSLATION = "item.drinkingflask.drinking_flask.fullness";

    public DrinkingFlaskItem(Settings settings) {
        super(settings);
    }

    public static FlaskContentsComponent getContents(ItemStack stack) {
        return stack.getOrDefault(DrinkingFlask.FLASK_CONTENTS, FlaskContentsComponent.DEFAULT);
    }

    public static int getSize(ItemStack stack) {
        return getContents(stack).getSize();
    }

    private static Integer getCapacity(ItemStack flaskStack) {
        return flaskStack.getOrDefault(DrinkingFlask.FLASK_CAPACITY, 0);
    }

    public static boolean canInsert(ItemStack itemStack) {
        return !(itemStack.getItem() instanceof DrinkingFlaskItem) && itemStack.isIn(DrinkingFlask.CAN_POUR_INTO_FLASK);
    }

    public static boolean itemFits(ItemStack flaskStack, ItemStack drinkStack) {
        int maxSize = getCapacity(flaskStack);
        var contents = getContents(flaskStack);
        return contents.getSize() + DrinkingFlask.getSize(drinkStack) <= maxSize;
    }

    public static ItemStack getRemainder(World world, ItemStack stack) {
        var input = new SingleStackRecipeInput(stack);
        return world.getRecipeManager()
                .getFirstMatch(DrinkingFlask.REMAINDER_RECIPE_TYPE, input, world)
                .map(entry -> entry.value().craft(input, world.getRegistryManager()))
                .orElseGet(stack::getRecipeRemainder); // returns EMPTY if none exists
    }

    public static ItemStack insertStack(ItemStack flaskStack, ItemStack drinkStack, World world, PlayerEntity user) {
        var remainder = getRemainder(world, drinkStack);
        FlaskContentsComponent.add(flaskStack, DrinkingFlask.FLASK_CONTENTS, drinkStack.splitUnlessCreative(1, user));

        // TODO add custom sound effect
        user.playSound(SoundEvents.ITEM_BOTTLE_FILL, 1f, 0.2f * world.random.nextFloat() + 0.6f);

        if (user.isInCreativeMode())
            return drinkStack;

        if (drinkStack.isEmpty())
            return remainder;

        user.getInventory().offerOrDrop(remainder);
        return drinkStack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var flaskStack = user.getStackInHand(hand);
        var otherHand = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
        var drinkStack = user.getStackInHand(otherHand);

        if (!canInsert(drinkStack) || !itemFits(flaskStack, drinkStack)) {
            if (getContents(flaskStack).isEmpty())
                return TypedActionResult.fail(flaskStack);

            return ItemUsage.consumeHeldItem(world, user, hand);
        }

        user.setStackInHand(otherHand, insertStack(flaskStack, drinkStack, world, user));

        return TypedActionResult.success(flaskStack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        super.finishUsing(stack, world, user);
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        if (!world.isClient) {
            var chosen = FlaskContentsComponent.popRandom(stack, DrinkingFlask.FLASK_CONTENTS, user.getRandom());
            chosen.finishUsing(world, user);
        }
        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 40;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType != ClickType.LEFT || !slot.canTakePartial(player)) return false;

        if (!canInsert(otherStack)) return false;
        if (!itemFits(stack, otherStack)) return false;

        cursorStackReference.set(insertStack(stack, otherStack, player.getWorld(), player));

        return true;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.LEFT) return false;

        ItemStack otherStack = slot.getStack();

        if (!canInsert(otherStack)) return false;
        if (!itemFits(stack, otherStack)) return false;

        slot.setStack(insertStack(stack, otherStack, player.getWorld(), player));
        return true;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return !getContents(stack).isEmpty();
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int maxSize = getCapacity(stack);
        if (maxSize <= 0) return 0;
        int size = getSize(stack);
        if (size == 0) return 0;
        if (size == maxSize) return 13;
        return min(11 * (size - 1) / (maxSize - 2), 11) + 1;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ITEM_BAR_COLOR;
    }

    public static void appendTooltip(ItemStack stack, TooltipContext context, TooltipType type, List<Text> tooltip) {
        int maxSize = getCapacity(stack);
        if (maxSize <= 0) return;
        tooltip.add(Text.translatable(FULLNESS_TRANSLATION, getSize(stack), maxSize).formatted(Formatting.GRAY));
    }
}
