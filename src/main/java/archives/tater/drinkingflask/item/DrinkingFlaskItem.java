package archives.tater.drinkingflask.item;

import archives.tater.drinkingflask.component.FlaskContentsComponent;
import archives.tater.drinkingflask.registry.DrinkingFlaskComponents;
import archives.tater.drinkingflask.registry.DrinkingFlaskItemTags;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.consume.UseAction;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;

import static java.lang.Math.min;

public class DrinkingFlaskItem extends Item {

    private static final int ITEM_BAR_COLOR = ColorHelper.fromFloats(1f, 0.4f, 0.4f, 1.0f);
    private static final ConsumableComponent FAKE_CONSUMABLE = ConsumableComponents.drink().consumeSeconds(2f).build();

    public DrinkingFlaskItem(Item.Settings settings) {
        super(settings);
    }

    public static FlaskContentsComponent getContents(ItemStack stack) {
        return stack.getOrDefault(DrinkingFlaskComponents.FLASK_CONTENTS, FlaskContentsComponent.DEFAULT);
    }

    public static int getFlaskSize(ItemStack stack) {
        return getContents(stack).getSize();
    }

    public static int getDrinkSize(ItemStack stack) {
        return stack.isIn(DrinkingFlaskItemTags.DOUBLE_SIZE) ? 2 : 1;
    }

    public static Integer getCapacity(ItemStack flaskStack) {
        return flaskStack.getOrDefault(DrinkingFlaskComponents.FLASK_CAPACITY, 0);
    }

    public static boolean canInsert(ItemStack itemStack) {
        if (itemStack.getItem() instanceof DrinkingFlaskItem) return false;
        if (itemStack.isIn(DrinkingFlaskItemTags.CAN_POUR_INTO_FLASK)) return true;
        var consumable = itemStack.get(DataComponentTypes.CONSUMABLE);
        if (consumable == null) return false;
        return consumable.useAction() == UseAction.DRINK;
    }

    public static boolean itemFits(ItemStack flaskStack, ItemStack drinkStack) {
        int maxSize = getCapacity(flaskStack);
        var contents = getContents(flaskStack);
        return contents.getSize() + getDrinkSize(drinkStack) <= maxSize;
    }

    public static ItemStack getRemainder(ItemStack stack) {
        var useRemainder = stack.get(DataComponentTypes.USE_REMAINDER);
        if (useRemainder != null) return useRemainder.convertInto().copy();
        var recipeRemainder = stack.getRecipeRemainder();
        if (recipeRemainder != null) return recipeRemainder.copy();
        return ItemStack.EMPTY;
    }

    public static ItemStack insertStack(ItemStack flaskStack, ItemStack drinkStack, World world, PlayerEntity user) {
        var remainder = getRemainder(drinkStack);
        FlaskContentsComponent.add(flaskStack, DrinkingFlaskComponents.FLASK_CONTENTS, drinkStack.splitUnlessCreative(1, user));

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
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        var flaskStack = user.getStackInHand(hand);
        var otherHand = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
        var drinkStack = user.getStackInHand(otherHand);

        if (!canInsert(drinkStack) || !itemFits(flaskStack, drinkStack)) {
            if (getContents(flaskStack).isEmpty())
                return ActionResult.FAIL;

            return ItemUsage.consumeHeldItem(world, user, hand);
        }

        user.setStackInHand(otherHand, insertStack(flaskStack, drinkStack, world, user));

        return ActionResult.SUCCESS.withNewHandStack(flaskStack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        super.finishUsing(stack, world, user);
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        if (!world.isClient()) {
            var chosen = FlaskContentsComponent.popRandom(stack, DrinkingFlaskComponents.FLASK_CONTENTS, user.getRandom());
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
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.usageTick(world, user, stack, remainingUseTicks);

        if (FAKE_CONSUMABLE.shouldSpawnParticlesAndPlaySounds(remainingUseTicks)) {
            FAKE_CONSUMABLE.spawnParticlesAndPlaySound(user.getRandom(), user, stack, 0);
        }
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType != ClickType.LEFT || !slot.canTakePartial(player)) return false;

        if (!canInsert(otherStack)) return false;
        if (!itemFits(stack, otherStack)) return false;

        cursorStackReference.set(insertStack(stack, otherStack, player.getEntityWorld(), player));

        return true;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.LEFT) return false;

        ItemStack otherStack = slot.getStack();

        if (!canInsert(otherStack)) return false;
        if (!itemFits(stack, otherStack)) return false;

        slot.setStack(insertStack(stack, otherStack, player.getEntityWorld(), player));
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
        int size = getFlaskSize(stack);
        if (size == 0) return 0;
        if (size == maxSize) return 13;
        return min(11 * (size - 1) / (maxSize - 2), 11) + 1;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ITEM_BAR_COLOR;
    }
}
