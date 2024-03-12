package archives.tater.drinkingflask.item;

import archives.tater.drinkingflask.DrinkingFlask;
import archives.tater.drinkingflask.client.FlaskTooltipData;
import archives.tater.drinkingflask.compat.DrinkingFlaskCompat;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class DrinkingFlaskItem extends Item {

    public static final String CONTENTS_KEY = "Contents";
    private static final int ITEM_BAR_COLOR = MathHelper.packRgb(0.4f, 0.4f, 1.0f);

    private final boolean showInTooltip;
    // TODO Make configurable later
    public final int maxSize;

    public DrinkingFlaskItem(int maxSize, boolean showInTooltip, Settings settings) {
        super(settings);
        this.maxSize = maxSize;
        this.showInTooltip = showInTooltip;
    }

    public static boolean canInsert(ItemStack itemStack) {
        return !(itemStack.getItem() instanceof DrinkingFlaskItem) && (
                itemStack.getUseAction() == UseAction.DRINK ||
                itemStack.getItem() instanceof StewItem ||
                itemStack.getItem() instanceof SuspiciousStewItem ||
                itemStack.isIn(DrinkingFlask.CAN_POUR_INTO_FLASK)
        );
    }

    public static NbtList getContents(ItemStack itemStack) {
        NbtCompound nbt = itemStack.getNbt();
        if (nbt == null) return new NbtList();
        return nbt.getList(CONTENTS_KEY, NbtElement.COMPOUND_TYPE);
    }

    public static void setContents(ItemStack itemStack, NbtList contents) {
        itemStack.setSubNbt(CONTENTS_KEY, contents);
    }

    public static void addContents(ItemStack itemStack, ItemStack addedStack) {
        NbtList contents = getContents(itemStack);
        contents.add(addedStack.writeNbt(new NbtCompound()));
        setContents(itemStack, contents);
    }

    // Only runs on server not client
    public static void applyEffect(ItemStack stack, World world, LivingEntity user) {
        DrinkingFlaskCompat.applyEffect(stack, world, user);

        // Honey bottles need a special case because they add a glass bottle to the inventory
        if (stack.isOf(Items.HONEY_BOTTLE)) {
            user.eatFood(world, stack);
            if (user instanceof ServerPlayerEntity serverPlayerEntity) {
                Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
                serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(Items.HONEY_BOTTLE));
            }
            user.removeStatusEffect(StatusEffects.POISON);
            return;
        }
        stack.finishUsing(world, user);
    }

    public static ItemStack getLeftover(ItemStack stack) {
        ItemStack compatResult = DrinkingFlaskCompat.getLeftover(stack);
        if (compatResult != null) return compatResult;

        if (stack.getItem().hasRecipeRemainder())
            return stack.getRecipeRemainder();
        if (stack.isOf(Items.POTION))
            return Items.GLASS_BOTTLE.getDefaultStack();
        if (stack.getItem() instanceof StewItem || stack.getItem() instanceof SuspiciousStewItem)
            return Items.BOWL.getDefaultStack();
        return ItemStack.EMPTY;
    }

    public static ItemStack insertStack(ItemStack flaskStack, ItemStack drinkStack, World world, PlayerEntity user) {
        addContents(flaskStack, drinkStack.copyWithCount(1));

        // TODO add custom sound effect
        user.playSound(SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1f, 0.2f * world.random.nextFloat() + 0.6f);

        if (user.getAbilities().creativeMode) {
            return drinkStack;
        }

        if (drinkStack.getCount() <= 1) {
            return getLeftover(drinkStack);
        }

        drinkStack.decrement(1);
        user.giveItemStack(getLeftover(drinkStack));
        return drinkStack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack flaskStack = user.getStackInHand(hand);
        NbtList contents = getContents(flaskStack);
        Hand otherHand = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
        ItemStack drinkStack = user.getStackInHand(otherHand);

        if (!canInsert(drinkStack) || contents.size() >= maxSize) {
            if (contents.isEmpty()) {
                return TypedActionResult.fail(flaskStack);
            }
            return ItemUsage.consumeHeldItem(world, user, hand);
        }

        ItemStack result = insertStack(flaskStack, drinkStack, world, user);
        if (result != flaskStack) {
            user.setStackInHand(otherHand, result);
        }

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
            NbtList contents = getContents(stack);
            if (contents.isEmpty()) return stack;
            int index = world.random.nextInt(contents.size());
            applyEffect(ItemStack.fromNbt((NbtCompound) contents.get(index)), world, user);
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

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType != ClickType.RIGHT || !slot.canTakePartial(player)) return false;

        if (getContents(stack).size() >= maxSize) return false;
        if (!canInsert(otherStack)) return false;

        cursorStackReference.set(insertStack(stack, otherStack, player.getWorld(), player));

        return true;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) return false;

        ItemStack otherStack = slot.getStack();

        if (getContents(stack).size() >= maxSize) return false;
        if (!canInsert(otherStack)) return false;

        slot.setStack(insertStack(stack, otherStack, player.getWorld(), player));
        return true;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return !getContents(stack).isEmpty();
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        NbtList contents = getContents(stack);
        int size = contents.size();
        return Math.min(13 * size / maxSize, 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ITEM_BAR_COLOR;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        int size = getContents(stack).size();
        tooltip.add(Text.translatable("item.drinkingflask.drinking_flask.fullness", size, maxSize).formatted(Formatting.GRAY));
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        if (!showInTooltip) return super.getTooltipData(stack);

        NbtList contents = getContents(stack);
        if (contents.isEmpty()) return super.getTooltipData(stack);
        DefaultedList<ItemStack> contentsList = DefaultedList.of();
        contents.forEach(nbt -> contentsList.add(ItemStack.fromNbt((NbtCompound) nbt)));
        return Optional.of(new FlaskTooltipData(contentsList, contents.size()));
    }
}
