package simpleclient.module.movement;

import org.lwjgl.glfw.GLFW;

import simpleclient.module.Mod;
import simpleclient.module.settings.NumberSetting;

public class Step extends Mod {
	
	public NumberSetting stepHeight = new NumberSetting("height", 0.0f, 5f, 1f, 0.5f);
	private float previousHeight = 1;
	
	public Step() {
		super("Step", "Makes you step higher", Category.MOVEMENT);
		this.setKey(GLFW.GLFW_KEY_KP_5);
		addSetting(stepHeight);
	}
	
	@Override
	public void onDisable() {
		if (mc.world != null) {
			mc.player.setStepHeight(.5f);
		}
		super.onDisable();
	}
	
	@Override
	public void onEnable() {
		mc.player.setStepHeight(stepHeight.getValueFloat());
		super.onEnable();
	}
	
	@Override
	public void onTick() {
		float stpHeight = 0f;
		if (mc.player.isSneaking()) {
			stpHeight = .5f;
		} else {
			previousHeight = stepHeight.getValueFloat();
			stpHeight = previousHeight;
		}
		if (stpHeight != mc.player.getStepHeight()) {
			mc.player.setStepHeight(stpHeight);
		}
		//mc.player.setBoundingBox(mc.player.getBoundingBox().offset(0, -1, 0));
		setInfo(": " + stepHeight.getValueFloat());
		super.onTick();
	}
	
	public void setStepHeight(float height) {
		this.stepHeight.setValue((double)height);
	}
}
