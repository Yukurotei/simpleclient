package simpleclient.module.movement;

import org.lwjgl.glfw.GLFW;

import simpleclient.module.Mod;

public class Sprint extends Mod {
	
	//public NumberSetting speed = new NumberSetting("Speed", 0, 10, 0.1, 0.1);
	
	public Sprint() {
		super("Sprint", "Toggle sprint", Category.MOVEMENT);
		this.setKey(GLFW.GLFW_KEY_KP_1);
		//addSetting(speed);
	}
	
	@Override
	public void onTick() {
		mc.player.setSprinting(true);
		//mc.player.setMovementSpeed(speed.getValueFloat());
		//setInfo(": " + speed.getValueFloat());
		super.onTick();
	}
}
