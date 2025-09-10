package simpleclient.module.movement;

import org.lwjgl.glfw.GLFW;

import simpleclient.Client;
import simpleclient.event.events.TickEvent;
import simpleclient.event.listeners.TickListener;
import simpleclient.module.Mod;
import simpleclient.module.settings.BooleanSetting;

public class Jesus extends Mod implements TickListener{

	public BooleanSetting legit = new BooleanSetting("legit", false);
	
	public Jesus() {
		super("Jesus", "Makes you walk on liquid", Category.MOVEMENT);
		this.setKey(GLFW.GLFW_KEY_KP_6);
		addSetting(legit);
	}
	
	@Override
	public void onEnable() {
		Client.getInstance().eventManager.AddListener(TickListener.class, this);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		Client.getInstance().eventManager.RemoveListener(TickListener.class, this);
		Client.getInstance().eventManager.RemoveListener(TickListener.class, this);
		super.onDisable();
	}
	
	@Override
	public void OnUpdate(TickEvent event) {
		// If we legit, we not jesus, we swim, we are fish
		if (legit.isEnabled()) {
			if (mc.player.isInLava() || mc.player.isTouchingWater()) {
				mc.options.jumpKey.setPressed(true);
			}
		}
	}
}
