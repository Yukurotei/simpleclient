package simpleclient.mixins;

import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import simpleclient.Client;
import simpleclient.ui.GUI;
import simpleclient.ui.GuiManager;
import simpleclient.ui.Hud;
import simpleclient.ui.screens.clickgui.ClickGUI;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	
	@Inject(method = "render", at = @At("RETURN"), cancellable=true)
	public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
		Hud.render(context, tickDelta);
		if (Client.INSTANCE.guiOpened) {
			GUI clickGui = GuiManager.INSTANCE.getGUI("ClickGUI");
			clickGui.render(context, (int)Math.round(Client.INSTANCE.mouseX), (int)Math.round(Client.INSTANCE.mouseY), tickDelta);
		}
        GUI hudGUI = GuiManager.INSTANCE.getGUI("HudElements");
        hudGUI.render(context, (int)Math.round(Client.INSTANCE.mouseX), (int)Math.round(Client.INSTANCE.mouseY), tickDelta);
	}
}
