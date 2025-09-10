package simpleclient.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import simpleclient.Client;
import simpleclient.event.events.MouseMoveEvent;
import simpleclient.ui.GuiManager;
import simpleclient.ui.screens.clickgui.ClickGUI;

@Mixin(Mouse.class)
public class MouseMixin
{
	@Inject(at = {@At("HEAD")}, method = {"lockCursor()V"}, cancellable = true)
	private void onLockCursor(CallbackInfo ci)
	{
		if(Client.INSTANCE.guiOpened) {
			ci.cancel();
		}
	}

	@Inject(at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/Mouse;updateMouse()V") }, method = {"onCursorPos(JDD)V" }, cancellable = true)
	private void onCursorPos(long window, double x, double y, CallbackInfo ci) {
		if(Client.INSTANCE.guiOpened) {
			Client.INSTANCE.mouseX = x * (double)Client.mc.getWindow().getScaledWidth() / (double)Client.mc.getWindow().getWidth();
			Client.INSTANCE.mouseY = y * (double)Client.mc.getWindow().getScaledHeight() / (double)Client.mc.getWindow().getHeight();
			MouseMoveEvent event = new MouseMoveEvent(x, y);
			Client.getInstance().eventManager.Fire(event);
			
			if(event.IsCancelled())
				ci.cancel();
		}
	}

	@Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
	private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
		if (Client.INSTANCE.guiOpened) {
			if (action == 1 && !Client.INSTANCE.mousePressedFired) {
				GuiManager.INSTANCE.getGUI("ClickGUI").mouseClicked(Client.INSTANCE.mouseX, Client.INSTANCE.mouseY, button);
                GuiManager.INSTANCE.getGUI("HudElements").mouseClicked(Client.INSTANCE.mouseX, Client.INSTANCE.mouseY, button);
				Client.INSTANCE.mousePressedFired = true;
				Client.INSTANCE.mouseReleasedFired = false;
			} else if (action != 1 && !Client.INSTANCE.mouseReleasedFired){
				GuiManager.INSTANCE.getGUI("ClickGUI").mouseReleased(Client.INSTANCE.mouseX, Client.INSTANCE.mouseY, button);
                GuiManager.INSTANCE.getGUI("HudElements").mouseReleased(Client.INSTANCE.mouseX, Client.INSTANCE.mouseY, button);
				Client.INSTANCE.mousePressedFired = false;
				Client.INSTANCE.mouseReleasedFired = true;
			}
			if (Client.mc.currentScreen == null) {
				ci.cancel();
			}
		}
	}

}