package archives.tater.drinkingflask.client.gui;

import archives.tater.drinkingflask.DrinkingFlask;
import archives.tater.drinkingflask.client.FlaskTooltipData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

@Environment(value= EnvType.CLIENT)
public class FlaskTooltipComponent implements TooltipComponent {
    public static final Identifier TEXTURE = new Identifier(DrinkingFlask.MOD_ID, "textures/gui/slot.png");
    private final DefaultedList<ItemStack> inventory;
    private static final int MAX_COLUMNS = 8;

    public FlaskTooltipComponent(FlaskTooltipData data) {
        inventory = data.inventory();
    }

    @Override
    public int getHeight() {
        return 18 * ((inventory.size() - 1) / MAX_COLUMNS + 1) + 4;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 18 * Math.min(inventory.size(), MAX_COLUMNS);
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int slots = inventory.size();
        for (int slot = 0; slot < slots; ++slot) {
            drawSlot(x + (slot % MAX_COLUMNS) * 18, y + (slot / MAX_COLUMNS) * 18, slot, context, textRenderer);
        }
    }

    private void drawSlot(int x, int y, int index, DrawContext context, TextRenderer textRenderer) {
        ItemStack itemStack = inventory.get(index);
        context.drawTexture(TEXTURE, x, y, 0, 0, 18, 18, 18, 18);
        context.drawItem(itemStack, x + 1, y + 1, index);
        context.drawItemInSlot(textRenderer, itemStack, x + 1, y + 1);
    }

}
