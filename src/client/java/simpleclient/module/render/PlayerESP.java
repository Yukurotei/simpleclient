package simpleclient.module.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import simpleclient.Client;
import simpleclient.event.events.RenderEvent;
import simpleclient.event.listeners.RenderListener;
import simpleclient.module.Mod;
import simpleclient.module.settings.ModeSetting;
import simpleclient.ui.Color;

public class PlayerESP extends Mod implements RenderListener {
	
	public ModeSetting renderMode = new ModeSetting("Mode", "Box", "Box", "Glow");
	
	public PlayerESP() {
		super("PlayerESP", "Allows you to see players", Category.RENDER);
		addSetting(renderMode);
	}
	
	@Override
	public void onEnable() {
		Client.getInstance().eventManager.AddListener(RenderListener.class, this);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
		Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
	
	@Override
	public void OnRender(RenderEvent event) {
		if (renderMode.isMode("Box")) {
			this.setInfo(": Box");
			for (AbstractClientPlayerEntity entity : mc.world.getPlayers()) {
				if (entity != mc.player) {
					if (Client.INSTANCE.doesFriendExist(entity.getName().getString())) {
						this.getRenderUtils().draw3DBox(event.GetMatrixStack(), entity.getBoundingBox(), new Color(5, 247, 243), 0.2f);
					} else {
						this.getRenderUtils().draw3DBox(event.GetMatrixStack(), entity.getBoundingBox(), new Color(255, 255, 255), 0.2f);
					}
				}
			}
		} else {
			this.setInfo(": Glow");
		}
	}

}
