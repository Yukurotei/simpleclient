package simpleclient.module.movement;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import simpleclient.Client;
import simpleclient.event.events.TickEvent;
import simpleclient.event.listeners.TickListener;
import simpleclient.module.Mod;
import simpleclient.module.settings.NumberSetting;

public class Noclip extends Mod implements TickListener {
	
	public NumberSetting flySpeed = new NumberSetting("flySpd", 0, 10, 5, 1);
	
	public Noclip() {
		super("Noclip[Client][NOT WORKING]", "Makes you go through blocks (Client side)", Category.MOVEMENT);
		this.setKey(GLFW.GLFW_KEY_KP_7);
	}
	
	public void setSpeed(float speed) {
		this.flySpeed.setValue((double)speed);
	}
	
	@Override
	public void onEnable() {
		Client.getInstance().eventManager.AddListener(TickListener.class, this);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		mc.player.noClip = false;
		Client.getInstance().eventManager.RemoveListener(TickListener.class, this);
		Client.getInstance().eventManager.RemoveListener(TickListener.class, this);
		super.onDisable();
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
	
	@Override
	public void OnUpdate(TickEvent event) {
		if (mc.player == null) return;
		ClientPlayerEntity player = mc.player;
		player.noClip = true;
		float speed = flySpeed.getValueFloat();
		if (mc.options.sprintKey.isPressed()) {
			setSpeed(speed *= 1.5);
		}
		player.setVelocity(new Vec3d(0, 0, 0));
		
		Vec3d vec = new Vec3d(0, 0, 0);
		if (mc.options.jumpKey.isPressed()) {
			vec = new Vec3d(0, speed * 0.2f, 0);
		}
		if (mc.options.sneakKey.isPressed()) {
			vec = new Vec3d(0, -speed * 0.2f, 0);
		}
		if (mc.options.sprintKey.isPressed()) {
			setSpeed(speed /= 1.5);
		}
		player.setVelocity(vec);
	}

}
