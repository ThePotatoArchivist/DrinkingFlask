package archives.tater.drinkingflask.client;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public record FlaskTooltipData(DefaultedList<ItemStack> inventory) implements TooltipData {}
