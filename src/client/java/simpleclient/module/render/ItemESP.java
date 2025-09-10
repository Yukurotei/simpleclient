package simpleclient.module.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import simpleclient.Client;
import simpleclient.event.events.RenderEvent;
import simpleclient.event.listeners.RenderListener;
import simpleclient.module.Mod;
import simpleclient.module.settings.ModeSetting;
import simpleclient.ui.Color;

public class ItemESP extends Mod implements RenderListener {
	
	public ModeSetting renderMode = new ModeSetting("Mode", "Box", "Box", "Glow");
	
	public ItemESP() {
		super("ItemESP", "Does what it says", Category.RENDER);
		addSetting(renderMode);
	}
	
	@Override
	public void onDisable() {
		Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
		Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
	}
	
	@Override
	public void onEnable() {
		Client.getInstance().eventManager.AddListener(RenderListener.class, this);
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
	
	@Override
	public void OnRender(RenderEvent event) {
		if (renderMode.isMode("Box")) {
			this.setInfo(": Box");
			for (Entity entity : mc.world.getEntities()) {
				if (entity instanceof ItemEntity) {
					this.getRenderUtils().draw3DBox(event.GetMatrixStack(), entity.getBoundingBox(), new Color(235, 52, 232), 0.2f);
				}
			}
		} else {
			this.setInfo(": Glow");
		}
	}
}
