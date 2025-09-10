package simpleclient.module.movement;

import java.util.UUID;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.util.math.Vec3d;
import simpleclient.Client;
import simpleclient.event.events.RenderEvent;
import simpleclient.event.listeners.RenderListener;
import simpleclient.misc.FakePlayerEntity;
import simpleclient.module.Mod;
import simpleclient.module.ModuleManager;
import simpleclient.module.settings.NumberSetting;
import simpleclient.ui.Color;

public class Freecam extends Mod implements RenderListener {
	
	private FakePlayerEntity fakePlayer;
	public NumberSetting flySpeed = new NumberSetting("flyspeed", 0.1f, 15f, 2f, 0.5f);
	public boolean isFlyEnabledBeforeExecuting = false;
	
	public Freecam() {
		super("Freecam", "Allows your camera to move anywhere it wants", Category.MOVEMENT);
		addSetting(flySpeed);
	}
	
	public void setSpeed(float speed) {
		this.flySpeed.setValue((double)speed);
	}
	
	public double getSpeed() {
		return this.flySpeed.getValue();
	}
	
	@Override
	public void onEnable() {
		ClientPlayerEntity player = mc.player;
		fakePlayer = new FakePlayerEntity();
		fakePlayer.copyFrom(player);
		fakePlayer.setUuid(UUID.randomUUID());
		fakePlayer.headYaw = player.headYaw;
		mc.world.addEntity(-3, fakePlayer);

		if (ModuleManager.INSTANCE.getModule("Flight") == null) {
			isFlyEnabledBeforeExecuting = false;
		} else {
			if (ModuleManager.INSTANCE.getModule("Flight").isEnabled()) {
				isFlyEnabledBeforeExecuting = true;
				((Flight)ModuleManager.INSTANCE.getModule("Flight")).setEnabled(false);
			}
		}
		Client.getInstance().eventManager.AddListener(RenderListener.class, this);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		if(mc.world == null || fakePlayer == null) return;
		ClientPlayerEntity player = mc.player;
		mc.player.noClip = false;
		player.setVelocity(0, 0, 0);
		player.copyFrom(fakePlayer);
		fakePlayer.despawn();
		mc.world.removeEntity(-3, RemovalReason.DISCARDED);
		if (isFlyEnabledBeforeExecuting) {
			isFlyEnabledBeforeExecuting = false;
			((Flight)ModuleManager.INSTANCE.getModule("Flight")).setEnabled(true);
		}
		Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
		Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
		super.onDisable();
	}
	
	public FakePlayerEntity getFakePlayer() {
		return this.fakePlayer;
	}
	
	@Override
	public void onTick() {
		ClientPlayerEntity player = mc.player;
		player.noClip = true;
		player.setOnGround(false);
		float speed = flySpeed.getValueFloat();
		if (mc.options.sprintKey.isPressed()) {
			speed *= 1.5;
		}
		
		player.getAbilities().flying = false;
		player.setVelocity(new Vec3d(0, 0, 0));
		Vec3d vec = new Vec3d(0,0,0);
		if (mc.options.jumpKey.isPressed()) {
			vec = new Vec3d(0,speed * 0.2f,0);
		}
		if (mc.options.sneakKey.isPressed()) {
			vec = new Vec3d(0,-speed * 0.2f,0);
		}

		player.setVelocity(vec);
		super.onTick();
	}

	@Override
	public void OnRender(RenderEvent event) {
		if (fakePlayer == null) return;
		this.getRenderUtils().draw3DBox(event.GetMatrixStack(), fakePlayer.getBoundingBox(), new Color(	255, 182, 193), 0.2f);
	}

}
