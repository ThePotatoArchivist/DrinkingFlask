package archives.tater.drinkingflask.client.gui;

import archives.tater.drinkingflask.DrinkingFlask;
import archives.tater.drinkingflask.component.FlaskContentsComponent;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.List;

import static java.lang.Math.min;

@Environment(value = EnvType.CLIENT)
public record FlaskTooltipComponent(List<ItemStack> contents) implements ClientTooltipComponent {
    public static final Identifier TEXTURE = DrinkingFlask.id("slot");
    private static final int MAX_COLUMNS = 8;

    public FlaskTooltipComponent(FlaskContentsComponent contents) {
        this(contents.contents().stream().map(ItemStackTemplate::create).toList());
    }

    @Override
    public int getHeight(Font textRenderer) {
        return 18 * ((contents.size() - 1) / MAX_COLUMNS + 1) + 4;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return 18 * min(contents.size(), MAX_COLUMNS);
    }


    @Override
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
        int slots = contents.size();
        for (int slot = 0; slot < slots; ++slot) {
            drawSlot(x + (slot % MAX_COLUMNS) * 18, y + (slot / MAX_COLUMNS) * 18, slot, graphics, font);
        }
    }

    private void drawSlot(int x, int y, int index, GuiGraphicsExtractor context, Font textRenderer) {
        ItemStack itemStack = contents.get(index);
        context.blitSprite(RenderPipelines.GUI_TEXTURED, TEXTURE, 18, 18, 0, 0, x, y, 18, 18);
        context.item(itemStack, x + 1, y + 1, index);
        context.itemDecorations(textRenderer, itemStack, x + 1, y + 1);
    }

}
