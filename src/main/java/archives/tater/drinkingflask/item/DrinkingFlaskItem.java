package archives.tater.drinkingflask.item;

import archives.tater.drinkingflask.component.FlaskContentsComponent;
import archives.tater.drinkingflask.registry.DrinkingFlaskComponents;
import archives.tater.drinkingflask.registry.DrinkingFlaskItemTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.Level;

import static java.lang.Math.min;

public class DrinkingFlaskItem extends Item {

    private static final int ITEM_BAR_COLOR = ARGB.colorFromFloat(1f, 0.4f, 0.4f, 1.0f);
    private static final Consumable FAKE_CONSUMABLE = Consumables.defaultDrink().consumeSeconds(2f).build();

    public DrinkingFlaskItem(Item.Properties settings) {
        super(settings);
    }

    public static FlaskContentsComponent getContents(ItemStack stack) {
        return stack.getOrDefault(DrinkingFlaskComponents.FLASK_CONTENTS, FlaskContentsComponent.DEFAULT);
    }

    public static int getFlaskSize(ItemStack stack) {
        return getContents(stack).getSize();
    }

    public static int getDrinkSize(ItemStack stack) {
        return stack.is(DrinkingFlaskItemTags.DOUBLE_SIZE) ? 2 : 1;
    }

    public static Integer getCapacity(ItemStack flaskStack) {
        return flaskStack.getOrDefault(DrinkingFlaskComponents.FLASK_CAPACITY, 0);
    }

    public static boolean canInsert(ItemStack itemStack) {
        if (itemStack.getItem() instanceof DrinkingFlaskItem) return false;
        if (itemStack.is(DrinkingFlaskItemTags.CAN_POUR_INTO_FLASK)) return true;
        var consumable = itemStack.get(DataComponents.CONSUMABLE);
        if (consumable == null) return false;
        return consumable.animation() == ItemUseAnimation.DRINK;
    }

    public static boolean itemFits(ItemStack flaskStack, ItemStack drinkStack) {
        int maxSize = getCapacity(flaskStack);
        var contents = getContents(flaskStack);
        return contents.getSize() + getDrinkSize(drinkStack) <= maxSize;
    }

    public static ItemStack getRemainder(ItemStack stack) {
        var useRemainder = stack.get(DataComponents.USE_REMAINDER);
        if (useRemainder != null) return useRemainder.convertInto().copy();
        var recipeRemainder = stack.getRecipeRemainder();
        if (recipeRemainder != null) return recipeRemainder.copy();
        return ItemStack.EMPTY;
    }

    public static ItemStack insertStack(ItemStack flaskStack, ItemStack drinkStack, Level world, Player user) {
        var remainder = getRemainder(drinkStack);
        FlaskContentsComponent.add(flaskStack, DrinkingFlaskComponents.FLASK_CONTENTS, drinkStack.consumeAndReturn(1, user));

        // TODO add custom sound effect
        user.playSound(SoundEvents.BOTTLE_FILL, 1f, 0.2f * world.random.nextFloat() + 0.6f);

        if (user.hasInfiniteMaterials())
            return drinkStack;

        if (drinkStack.isEmpty())
            return remainder;

        user.getInventory().placeItemBackInInventory(remainder);
        return drinkStack;
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        var flaskStack = user.getItemInHand(hand);
        var otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        var drinkStack = user.getItemInHand(otherHand);

        if (!canInsert(drinkStack) || !itemFits(flaskStack, drinkStack)) {
            if (getContents(flaskStack).isEmpty())
                return InteractionResult.FAIL;

            return ItemUtils.startUsingInstantly(world, user, hand);
        }

        user.setItemInHand(otherHand, insertStack(flaskStack, drinkStack, world, user));

        return InteractionResult.SUCCESS.heldItemTransformedTo(flaskStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        super.finishUsingItem(stack, world, user);
        if (user instanceof ServerPlayer serverPlayerEntity) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.awardStat(Stats.ITEM_USED.get(this));
        }
        if (!world.isClientSide()) {
            var chosen = FlaskContentsComponent.popRandom(stack, DrinkingFlaskComponents.FLASK_CONTENTS, user.getRandom());
            chosen.finishUsingItem(world, user);
        }
        return stack;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 40;
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.onUseTick(world, user, stack, remainingUseTicks);

        if (FAKE_CONSUMABLE.shouldEmitParticlesAndSounds(remainingUseTicks)) {
            FAKE_CONSUMABLE.emitParticlesAndSounds(user.getRandom(), user, stack, 0);
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
        if (clickType != ClickAction.PRIMARY || !slot.allowModification(player)) return false;

        if (!canInsert(otherStack)) return false;
        if (!itemFits(stack, otherStack)) return false;

        cursorStackReference.set(insertStack(stack, otherStack, player.level(), player));

        return true;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickType, Player player) {
        if (clickType != ClickAction.PRIMARY) return false;

        ItemStack otherStack = slot.getItem();

        if (!canInsert(otherStack)) return false;
        if (!itemFits(stack, otherStack)) return false;

        slot.setByPlayer(insertStack(stack, otherStack, player.level(), player));
        return true;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return !getContents(stack).isEmpty();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int maxSize = getCapacity(stack);
        if (maxSize <= 0) return 0;
        int size = getFlaskSize(stack);
        if (size == 0) return 0;
        if (size == maxSize) return 13;
        return min(11 * (size - 1) / (maxSize - 2), 11) + 1;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return ITEM_BAR_COLOR;
    }
}
