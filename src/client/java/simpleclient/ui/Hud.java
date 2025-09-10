package simpleclient.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import simpleclient.Client;
import simpleclient.module.Mod;
import simpleclient.module.Mod.Category;
import simpleclient.module.ModuleManager;
import simpleclient.ui.screens.HudElement;

public class Hud {
	
	private static MinecraftClient mc = MinecraftClient.getInstance();

    private static List<HudElement> huds = new ArrayList<>();

    public static void addElement(HudElement element) {
        huds.add(element);
    }

    public static List<HudElement> getHudElements() {
        return huds;
    }
	
	public static void render(DrawContext context, float tickDelta) {
		renderArrayList(context);
	}

	public static void renderArrayList(DrawContext context) {
		int index = 0;
		int sWidth = mc.getWindow().getScaledWidth();
		//int sHeight = mc.getWindow().getScaledWidth();
		
		List<Mod> enabled = ModuleManager.INSTANCE.getEnabledModules();
		List<Mod> enabledSettings = ModuleManager.INSTANCE.getModulesInCategory(Category.SETTING);
		
		enabled.sort(Comparator.comparingInt(m -> (int)mc.textRenderer.getWidth(((Mod)m).getDisplayName())).reversed());
		
		int clr;
		
		if (Client.INSTANCE.rainbowHudColor != null && Client.INSTANCE.rainbowHud) {
			clr = new Color(Client.INSTANCE.rainbowHudColor.getRedFloat(), Client.INSTANCE.rainbowHudColor.getGreenFloat(), Client.INSTANCE.rainbowHudColor.getBlueFloat(), 1.0f).getRGB();
		} else {
			clr = 0;
		}
		
		if (Client.INSTANCE.specialHud) {
			if (Client.INSTANCE.specialHudDisplayMode == Client.HudDisplayMode.ASCORDS && Client.INSTANCE.specialHudCordsColor != 0 && Client.INSTANCE.specialHudDirectionsColor != 0) {
				String text = "(" + mc.player.getBlockX() + ", " + mc.player.getBlockY() + ", " + mc.player.getBlockZ() + ") ";
				String facing = mc.player.getHorizontalFacing().asString().toUpperCase();
				int left1 = -1 + mc.textRenderer.getWidth(facing + "pla");
				int right1 = (sWidth - 4) - mc.textRenderer.getWidth(text + facing) - 2;
				int left2 = -1 + mc.textRenderer.getWidth("pl");
				int right2 = (sWidth - 4) - mc.textRenderer.getWidth(facing) - 2;
				context.drawText(mc.textRenderer, text, Client.INSTANCE.specialHudDisplaySide == Client.HudDisplaySide.RIGHT ? right1 : left1, mc.textRenderer.fontHeight, Client.INSTANCE.specialHudCordsColor, true);
				context.drawText(mc.textRenderer, facing, Client.INSTANCE.specialHudDisplaySide == Client.HudDisplaySide.RIGHT ? right2 : left2, mc.textRenderer.fontHeight, Client.INSTANCE.specialHudDirectionsColor, true);
				if (!Client.INSTANCE.displayModules) return;
			} else if (Client.INSTANCE.specialHudDisplayMode == Client.HudDisplayMode.INVIS) {
				return;
			}
		}
		
		for (Mod mod : enabled) {
			if (mod.category != Category.SETTING) {
				context.drawText(mc.textRenderer, mod.getInfo() != null ? mod.getDisplayName() + mod.getInfo() : mod.getDisplayName(), (sWidth - 4) - mc.textRenderer.getWidth(mod.getInfo() != null ? mod.getDisplayName() + mod.getInfo() : mod.getDisplayName()), 10 + (index * mc.textRenderer.fontHeight), clr == 0 ? Color.CYAN.getRGB() : clr, true);
				index++;
			}
		}
		for (Mod mod : enabledSettings) {
			if (mod.isEnabled()) {
				context.drawText(mc.textRenderer, "━━━━━", (sWidth - 4) - mc.textRenderer.getWidth("━━━━━"), 10 + (index * mc.textRenderer.fontHeight), clr == 0 ? Color.CYAN.getRGB() : clr, true);
				index++;
				break;
			}
		}
		for (Mod mod : enabledSettings) {
			if (mod.isEnabled()) {
				context.drawText(mc.textRenderer, mod.getInfo() != null ? mod.getDisplayName() + mod.getInfo() : mod.getDisplayName(), (sWidth - 4) - mc.textRenderer.getWidth(mod.getInfo() != null ? mod.getDisplayName() + mod.getInfo() : mod.getDisplayName()), 10 + (index * mc.textRenderer.fontHeight), clr == 0 ? Color.CYAN.getRGB() : clr, true);
				index++;
			}
		}
	}
}
