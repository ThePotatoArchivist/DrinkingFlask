package archives.tater.drinkingflask.component;

import archives.tater.drinkingflask.item.DrinkingFlaskItem;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record FlaskContentsComponent(List<ItemStack> contents) implements TooltipComponent {

    public int getSize() {
        return contents.stream().mapToInt(DrinkingFlaskItem::getDrinkSize).sum();
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public FlaskContentsComponent withAdded(ItemStack stack) {
        var newContents = new ArrayList<>(contents);
        newContents.add(stack);
        return new FlaskContentsComponent(newContents);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlaskContentsComponent component = (FlaskContentsComponent) o;
        return ItemStack.listMatches(this.contents, component.contents);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int hashCode() {
        return ItemStack.hashStackList(contents);
    }

    public static ItemStack popRandom(ItemStack container, DataComponentType<FlaskContentsComponent> type, RandomSource random) {
        var component = container.getOrDefault(type, DEFAULT);
        var contents = new ArrayList<>(component.contents);
        if (contents.isEmpty()) return ItemStack.EMPTY;
        var stack = contents.remove(random.nextInt(contents.size()));
        container.set(type, new FlaskContentsComponent(contents));
        return stack;
    }

    public static void add(ItemStack container, DataComponentType<FlaskContentsComponent> type, ItemStack stack) {
        container.set(type, container.getOrDefault(type, DEFAULT).withAdded(stack));
    }

    public static final FlaskContentsComponent DEFAULT = new FlaskContentsComponent(List.of());

    public static final Codec<FlaskContentsComponent> CODEC = ItemStack.CODEC.sizeLimitedListOf(99).xmap(FlaskContentsComponent::new, FlaskContentsComponent::contents);

    public static final StreamCodec<RegistryFriendlyByteBuf, FlaskContentsComponent> PACKET_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list(99)).map(FlaskContentsComponent::new, FlaskContentsComponent::contents);
}
