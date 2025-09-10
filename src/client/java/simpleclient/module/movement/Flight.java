package simpleclient.module.movement;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import simpleclient.Client;
import simpleclient.event.events.TickEvent;
import simpleclient.event.listeners.TickListener;
import simpleclient.module.Mod;
import simpleclient.module.settings.ModeSetting;
import simpleclient.module.settings.NumberSetting;

public class Flight extends Mod implements TickListener{
	
	public NumberSetting flySpeed = new NumberSetting("Speed", 0.1f, 15f, 2f, 0.5f);
	public ModeSetting flyMethod = new ModeSetting("M", "vlct", "vlct", "ablt");
	private enum FlyMode {
		VELOCITY,
		ABILITY
	}
	private FlyMode currentMethod = FlyMode.VELOCITY;
	
	public Flight() {
		super("Flight", "Basic flying", Category.MOVEMENT);
		this.setKey(GLFW.GLFW_KEY_KP_0);
		addSettings(flySpeed, flyMethod);
	}
	
	public float getSpeed() {
		return flySpeed.getValueFloat();
	}
	
	@Override
	public void onTick() {
		if (flyMethod.isMode("vlct")) {
			currentMethod = FlyMode.VELOCITY;
		} else if (flyMethod.isMode("ablt")) {
			currentMethod = FlyMode.ABILITY;
		}
		if (currentMethod == FlyMode.VELOCITY) {
			mc.player.getAbilities().flying = false;
		} else if (currentMethod == FlyMode.ABILITY) {
			mc.player.getAbilities().allowFlying = true;
		}
		super.onTick();
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
		if (currentMethod == FlyMode.ABILITY) {
			mc.player.getAbilities().flying = false;
			mc.player.getAbilities().allowFlying = false;
		}
		super.onDisable();
	}
	
	@Override
	public void OnUpdate(TickEvent event) {
		if (mc.player != null) {
			ClientPlayerEntity player = mc.player;
			float speed = flySpeed.getValueFloat();
			if (currentMethod == FlyMode.VELOCITY) {
                if (mc.player.isSprinting()) {
                    speed *= 1.5;
                }
                player.getAbilities().flying = false;
                player.setVelocity(new Vec3d(0, 0, 0));
                double motionX = 0;
                double motionY = 0;
                double motionZ = 0;
                if (mc.options.jumpKey.isPressed()) {
                    motionY += speed * 0.4f;
                }
                if (mc.options.sneakKey.isPressed()) {
                    motionY += -speed * 0.4f;
                }
                float playerYaw = player.getYaw();
                if (mc.options.leftKey.isPressed()) {
                    motionX = ((speed * 0.4f) * (Math.cos(playerYaw * 3.14 / 180)));
                    motionZ = ((speed * 0.4f) * (Math.sin(playerYaw * 3.14 / 180)));
                }
                if (mc.options.rightKey.isPressed()) {
                    motionX = -((speed * 0.4f) * (Math.cos(playerYaw * 3.14 / 180)));
                    motionZ = -((speed * 0.4f) * (Math.sin(playerYaw * 3.14 / 180)));
                }
                if (mc.options.forwardKey.isPressed()) {
                    motionX = -((speed * 0.4f) * (Math.cos((playerYaw - 90) * 3.14 / 180)));
                    motionZ = -((speed * 0.4f) * (Math.sin((playerYaw - 90) * 3.14 / 180)));
                }
                if (mc.options.backKey.isPressed()) {
                    motionX = ((speed * 0.4f) * (Math.cos((playerYaw - 90) * 3.14 / 180)));
                    motionZ = ((speed * 0.4f) * (Math.sin((playerYaw - 90) * 3.14 / 180)));
                }
                Vec3d vec = new Vec3d(motionX, motionY, motionZ);
                //Check if player is riding, if is then apply to vehicle instead
                if (mc.player.isRiding()) {
                    Entity riding = mc.player.getRootVehicle();
                    riding.setVelocity(vec);
                } else {
                    player.setVelocity(vec);
                }
			} else if (currentMethod == FlyMode.ABILITY) {
				mc.player.getAbilities().flying = true;
				mc.player.getAbilities().setFlySpeed(speed / 4);
			}
			setInfo(": " + flySpeed.getValueFloat());
		}
	}
	
}
