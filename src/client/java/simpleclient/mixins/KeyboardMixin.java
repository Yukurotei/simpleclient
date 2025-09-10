package simpleclient.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Keyboard;
import simpleclient.Client;
import simpleclient.ui.GuiManager;
import simpleclient.ui.screens.clickgui.ClickGUI;

@Mixin(Keyboard.class)
public class KeyboardMixin {
	
	@Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
	public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
		Client.INSTANCE.onKeyPress(key, action);
		GuiManager.INSTANCE.getGUI("ClickGUI").onKey(key);
        GuiManager.INSTANCE.getGUI("HudElements").onKey(key);
	}
}
