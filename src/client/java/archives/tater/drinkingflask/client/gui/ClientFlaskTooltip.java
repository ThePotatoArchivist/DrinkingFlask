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
import net.minecraft.world.item.ItemStackTemplate;

import java.util.List;

import static java.lang.Math.min;

@Environment(value = EnvType.CLIENT)
public record ClientFlaskTooltip(List<ItemStackTemplate> contents) implements ClientTooltipComponent {
    public static final Identifier TEXTURE = DrinkingFlask.id("slot");
    private static final int MAX_COLUMNS = 8;

    public ClientFlaskTooltip(FlaskContentsComponent contents) {
        this(contents.contents());
    }

    @Override
    public int getHeight(Font fonts) {
        return 18 * ((contents.size() - 1) / MAX_COLUMNS + 1) + 4;
    }

    @Override
    public int getWidth(Font font) {
        return 18 * min(contents.size(), MAX_COLUMNS);
    }

    @Override
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
        var slots = contents.size();
        for (int slot = 0; slot < slots; ++slot) {
            extractSlot(x + (slot % MAX_COLUMNS) * 18, y + (slot / MAX_COLUMNS) * 18, slot, graphics, font);
        }
    }

    private void extractSlot(int x, int y, int index, GuiGraphicsExtractor graphics, Font font) {
        var stack = contents.get(index).create();
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TEXTURE, 18, 18, 0, 0, x, y, 18, 18);
        graphics.item(stack, x + 1, y + 1, index);
        graphics.itemDecorations(font, stack, x + 1, y + 1);
    }

}
