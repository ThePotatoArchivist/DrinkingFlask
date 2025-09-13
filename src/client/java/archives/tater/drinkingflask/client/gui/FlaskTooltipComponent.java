package archives.tater.drinkingflask.client.gui;

import archives.tater.drinkingflask.DrinkingFlask;
import archives.tater.drinkingflask.component.FlaskContentsComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;

@Environment(value = EnvType.CLIENT)
public record FlaskTooltipComponent(List<ItemStack> contents) implements TooltipComponent {
    public static final Identifier TEXTURE = DrinkingFlask.id("slot");
    private static final int MAX_COLUMNS = 8;

    public FlaskTooltipComponent(FlaskContentsComponent contents) {
        this(contents.contents());
    }

    @Override
    public int getHeight(TextRenderer textRenderer) {
        return 18 * ((contents.size() - 1) / MAX_COLUMNS + 1) + 4;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 18 * Math.min(contents.size(), MAX_COLUMNS);
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
        int slots = contents.size();
        for (int slot = 0; slot < slots; ++slot) {
            drawSlot(x + (slot % MAX_COLUMNS) * 18, y + (slot / MAX_COLUMNS) * 18, slot, context, textRenderer);
        }
    }

    private void drawSlot(int x, int y, int index, DrawContext context, TextRenderer textRenderer) {
        ItemStack itemStack = contents.get(index);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, 18, 18, 0, 0, x, y, 18, 18);
        context.drawItem(itemStack, x + 1, y + 1, index);
        context.drawStackOverlay(textRenderer, itemStack, x + 1, y + 1);
    }

}
