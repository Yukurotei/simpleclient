package simpleclient.module.setting;

import simpleclient.Client;
import simpleclient.module.Mod;
import simpleclient.module.settings.BooleanSetting;
import simpleclient.module.settings.ModeSetting;
import simpleclient.module.settings.NumberSetting;
import simpleclient.ui.Color;

public class ModifyHUD extends Mod {
	
	public ModeSetting displayModeOption = new ModeSetting("dpmde", "cords", "cords", "invis");
	public ModeSetting sideOption = new ModeSetting("side", "left", "left", "right");
	public NumberSetting cordsHue = new NumberSetting("CHue", 0, 360, 170, 1);
	public NumberSetting directionsHue = new NumberSetting("DHue", 0, 360, 185, 1);
	public BooleanSetting displayModules = new BooleanSetting("DM", true);
	private Color cordsColor;
	private Color directionsColor;
	
	public ModifyHUD() {
		super("ModifyHUD", "Modifies the hud", Category.SETTING);
		cordsColor = new Color(196f, 1f, 1f);
		directionsColor = new Color(0, 0, 0);
		addSettings(displayModeOption, sideOption, cordsHue, directionsHue, displayModules);
	}
	
	@Override
	public void onEnable() {
		Client.INSTANCE.specialHud = true;
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		Client.INSTANCE.specialHud = false;
		super.onDisable();
	}
	
	@Override
	public void onTick() {
		if (displayModeOption.isMode("cords")) {
			Client.INSTANCE.specialHudDisplayMode = Client.HudDisplayMode.ASCORDS;
		} else if (displayModeOption.isMode("invis")) {
			Client.INSTANCE.specialHudDisplayMode = Client.HudDisplayMode.INVIS;
		}
		if (sideOption.isMode("left")) {
			Client.INSTANCE.specialHudDisplaySide = Client.HudDisplaySide.LEFT;
		} else if (sideOption.isMode("right")) {
			if (displayModules.isEnabled()) {
				Client.INSTANCE.specialHudDisplaySide = Client.HudDisplaySide.LEFT;
				sideOption.setMode("left");
			} else {
				Client.INSTANCE.specialHudDisplaySide = Client.HudDisplaySide.RIGHT;
			}
		}
		Client.INSTANCE.displayModules = displayModules.isEnabled();
		this.cordsColor.setHSV(cordsHue.getValueFloat(), 1f, 1f);
		this.directionsColor.setHSV(directionsHue.getValueFloat(), 1f, 1f);
		Client.INSTANCE.specialHudCordsColor = this.cordsColor.getColorAsInt();
		Client.INSTANCE.specialHudDirectionsColor = this.directionsColor.getColorAsInt();
		super.onTick();
	}

}
