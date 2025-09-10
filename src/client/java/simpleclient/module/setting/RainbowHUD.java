package simpleclient.module.setting;

import simpleclient.Client;
import simpleclient.event.events.TickEvent;
import simpleclient.event.listeners.TickListener;
import simpleclient.misc.RainbowColor;
import simpleclient.module.Mod;
import simpleclient.module.settings.NumberSetting;
import simpleclient.ui.Color;

public class RainbowHUD extends Mod implements TickListener{

	private Color currentColor;
	private Color color;
	private RainbowColor rainbowColor;
	
	public NumberSetting effectSpeed = new NumberSetting("Efspd", 10, 50, 20, 1);
	
	public RainbowHUD() {
		super("RainbowHUD", "Makes the hud rainbow", Category.SETTING);
		color = new Color(196f, 1f, 1f);
		currentColor = color;
		rainbowColor = new RainbowColor();
		addSetting(effectSpeed);
	}
	
	@Override
	public void onEnable() {
		Client.INSTANCE.rainbowHud = true;
		Client.getInstance().eventManager.AddListener(TickListener.class, this);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		Client.INSTANCE.rainbowHud = false;
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
		this.rainbowColor.update(this.effectSpeed.getValueFloat());
		this.currentColor = this.rainbowColor.getColor();
		Client.INSTANCE.rainbowHudColor = this.currentColor;
	}
}
