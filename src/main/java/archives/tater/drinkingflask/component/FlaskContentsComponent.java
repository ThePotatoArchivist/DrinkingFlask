package archives.tater.drinkingflask.component;

import archives.tater.drinkingflask.item.DrinkingFlaskItem;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.ArrayList;
import java.util.List;

public record FlaskContentsComponent(List<ItemStackTemplate> contents) implements TooltipComponent {

    public int getSize() {
        return contents.stream().mapToInt(DrinkingFlaskItem::getDrinkSize).sum();
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public FlaskContentsComponent withAdded(ItemStack stack) {
        var newContents = new ArrayList<>(contents);
        newContents.add(ItemStackTemplate.fromNonEmptyStack(stack));
        return new FlaskContentsComponent(newContents);
    }

    public static ItemStack popRandom(ItemStack container, DataComponentType<FlaskContentsComponent> type, RandomSource random) {
        var component = container.getOrDefault(type, DEFAULT);
        if (component.contents.isEmpty()) return ItemStack.EMPTY;
        var contents = new ArrayList<>(component.contents);
        var stack = contents.remove(random.nextInt(contents.size()));
        container.set(type, new FlaskContentsComponent(contents));
        return stack.create();
    }

    public static void add(ItemStack container, DataComponentType<FlaskContentsComponent> type, ItemStack stack) {
        container.set(type, container.getOrDefault(type, DEFAULT).withAdded(stack));
    }

    public static final FlaskContentsComponent DEFAULT = new FlaskContentsComponent(List.of());

    public static final Codec<FlaskContentsComponent> CODEC = ItemStackTemplate.CODEC.sizeLimitedListOf(99).xmap(FlaskContentsComponent::new, FlaskContentsComponent::contents);

    public static final StreamCodec<RegistryFriendlyByteBuf, FlaskContentsComponent> STREAM_CODEC = ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list(99)).map(FlaskContentsComponent::new, FlaskContentsComponent::contents);
}
