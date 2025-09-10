package simpleclient.module.render;

import org.lwjgl.glfw.GLFW;

import simpleclient.event.events.GammaCallback;
import simpleclient.module.Mod;

public class FullBright extends Mod {
	
	public FullBright() {
		super("FullBright", "Toggle full brightness", Category.RENDER);
		this.setKey(GLFW.GLFW_KEY_KP_3);
	}
	
	@Override
	public void onEnable() {
		GammaCallback.EVENT.register(gamma -> {
            gamma = 15f;
            return gamma;
        });
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		GammaCallback.EVENT.register(gamma -> {
            gamma = 0f;
            return gamma;
        });
		super.onDisable();
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
}
