package archives.tater.drinkingflask.client.gui;

import archives.tater.drinkingflask.DrinkingFlask;
import archives.tater.drinkingflask.component.FlaskContentsComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public record FlaskTooltipComponent(List<ItemStack> contents) implements ClientTooltipComponent {
    public static final Identifier TEXTURE = DrinkingFlask.id("slot");
    private static final int MAX_COLUMNS = 8;

    public FlaskTooltipComponent(FlaskContentsComponent contents) {
        this(contents.contents());
    }

    @Override
    public int getHeight(Font textRenderer) {
        return 18 * ((contents.size() - 1) / MAX_COLUMNS + 1) + 4;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return 18 * Math.min(contents.size(), MAX_COLUMNS);
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, int width, int height, GuiGraphics context) {
        int slots = contents.size();
        for (int slot = 0; slot < slots; ++slot) {
            drawSlot(x + (slot % MAX_COLUMNS) * 18, y + (slot / MAX_COLUMNS) * 18, slot, context, textRenderer);
        }
    }

    private void drawSlot(int x, int y, int index, GuiGraphics context, Font textRenderer) {
        ItemStack itemStack = contents.get(index);
        context.blitSprite(RenderPipelines.GUI_TEXTURED, TEXTURE, 18, 18, 0, 0, x, y, 18, 18);
        context.renderItem(itemStack, x + 1, y + 1, index);
        context.renderItemDecorations(textRenderer, itemStack, x + 1, y + 1);
    }

}
