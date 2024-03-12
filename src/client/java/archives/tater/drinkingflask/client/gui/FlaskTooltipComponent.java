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
    private final int occupancy;

    public FlaskTooltipComponent(FlaskTooltipData data) {
        inventory = data.inventory();
        occupancy = data.bundleOccupancy();
    }

    @Override
    public int getHeight() {
        return 18 + 4;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 18 * inventory.size();
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int columns = inventory.size();
        for (int column = 0; column < columns; ++column) {
            drawSlot(x + column * 18, y, column, context, textRenderer);
        }
    }

    private void drawSlot(int x, int y, int index, DrawContext context, TextRenderer textRenderer) {
        ItemStack itemStack = inventory.get(index);
        context.drawTexture(TEXTURE, x, y, 0, 0, 18, 18, 18, 18);
        context.drawItem(itemStack, x + 1, y + 1, index);
        context.drawItemInSlot(textRenderer, itemStack, x + 1, y + 1);
    }

}
