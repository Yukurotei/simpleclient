package simpleclient.utils;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class StringUtil extends Utils {

    public static void shrinkTextToWidthAndDraw(DrawContext context, int width, TextRenderer renderer, String text, int x, int y, int color, boolean shadow) {
        int textWidth = renderer.getWidth(text);

        float scale = 1.0f;
        if (textWidth > width) {
            scale = (float) width / (float) textWidth;
        }

        context.getMatrices().push();

        context.getMatrices().translate(x, y, 0);
        context.getMatrices().scale(scale, scale, 1.0f);
        context.drawText(renderer, text, 0, 0, color, shadow);

        context.getMatrices().pop();
    }
}
