package simpleclient.module.movement;

import org.lwjgl.glfw.GLFW;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import simpleclient.Client;
import simpleclient.event.events.SendPacketEvent;
import simpleclient.event.listeners.SendPacketListener;
//import simpleclient.interfaces.IPlayerMoveC2SPacket;
import simpleclient.mixins.PlayerMoveC2SPacketAccessor;
import simpleclient.module.Mod;
import simpleclient.module.ModuleManager;

public class NoFallDamage extends Mod implements SendPacketListener {

	public NoFallDamage() {
		super("No-fall", "Cancels fall damage", Category.MOVEMENT);
		this.setKey(GLFW.GLFW_KEY_KP_4);
	}
	
	@Override
	public void onEnable() {
		Client.getInstance().eventManager.AddListener(SendPacketListener.class, this);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		Client.getInstance().eventManager.RemoveListener(SendPacketListener.class, this);
		Client.getInstance().eventManager.RemoveListener(SendPacketListener.class, this);
		super.onDisable();
	}
	
	@Override
	public void onTick() {
		/*
		if (mc.player == null) return;
		if (ModuleManager.INSTANCE.getModule("Flight").isEnabled()) {
			mc.player.networkHandler.sendPacket(new OnGroundOnly(true));
		} else {
			if (mc.player.fallDistance > 2f) {
				mc.player.networkHandler.sendPacket(new OnGroundOnly(true));
			} else {
				return;
			}
		}
		*/
		super.onTick();
	}
	
	@Override
	public void OnSendPacket(SendPacketEvent event) {
		if (mc.player == null) return;
		if (mc.player.getAbilities().creativeMode
			|| !(event.GetPacket() instanceof PlayerMoveC2SPacket)
			//|| ((IPlayerMoveC2SPacket) event.GetPacket()).getTag() == 1337) return;
			) return;
		if (!ModuleManager.INSTANCE.getModule("Flight").isEnabled()) {
			if (mc.player.isFallFlying()) return;
            if (mc.player.getVelocity().y > -0.5) return;
            ((PlayerMoveC2SPacketAccessor) event.GetPacket()).setOnGround(true);
		} else {
			((PlayerMoveC2SPacketAccessor) event.GetPacket()).setOnGround(true);
		}
	}
}
