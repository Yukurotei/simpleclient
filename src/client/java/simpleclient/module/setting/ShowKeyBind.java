package simpleclient.module.setting;

import simpleclient.Client;
import simpleclient.module.Mod;
import simpleclient.module.ModuleManager;
import simpleclient.module.settings.KeyBindSetting;
import simpleclient.module.settings.Setting;

public class ShowKeyBind extends Mod {
	
	public ShowKeyBind() {
		super("ShowKeyBind", "Shows keybind option", Category.SETTING);
	}
	
	@Override
	public void onEnable() {
		for (Mod mod : ModuleManager.INSTANCE.getModules()) {
			for (Setting setting : mod.getSettings()) {
				if (setting instanceof KeyBindSetting) {
					Client.INSTANCE.showKeyBind = true;
					setting.setVisible(true);
				}
			}
		}
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		for (Mod mod : ModuleManager.INSTANCE.getModules()) {
			for (Setting setting : mod.getSettings()) {
				if (setting instanceof KeyBindSetting) {
					Client.INSTANCE.showKeyBind = false;
					setting.setVisible(false);
				}
			}
		}
		super.onDisable();
	}

}
