package archives.tater.drinkingflask.component;

import archives.tater.drinkingflask.item.DrinkingFlaskItem;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public record FlaskContentsComponent(List<ItemStack> contents) implements TooltipData {

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
        return ItemStack.stacksEqual(this.contents, component.contents);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int hashCode() {
        return ItemStack.listHashCode(contents);
    }

    public static ItemStack popRandom(ItemStack container, ComponentType<FlaskContentsComponent> type, Random random) {
        var component = container.getOrDefault(type, DEFAULT);
        var contents = new ArrayList<>(component.contents);
        if (contents.isEmpty()) return ItemStack.EMPTY;
        var stack = contents.remove(random.nextInt(contents.size()));
        container.set(type, new FlaskContentsComponent(contents));
        return stack;
    }

    public static void add(ItemStack container, ComponentType<FlaskContentsComponent> type, ItemStack stack) {
        container.set(type, container.getOrDefault(type, DEFAULT).withAdded(stack));
    }

    public static final FlaskContentsComponent DEFAULT = new FlaskContentsComponent(List.of());

    public static final Codec<FlaskContentsComponent> CODEC = ItemStack.CODEC.sizeLimitedListOf(99).xmap(FlaskContentsComponent::new, FlaskContentsComponent::contents);

    public static final PacketCodec<RegistryByteBuf, FlaskContentsComponent> PACKET_CODEC = ItemStack.PACKET_CODEC.collect(PacketCodecs.toList(99)).xmap(FlaskContentsComponent::new, FlaskContentsComponent::contents);
}
