package simpleclient.module;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import simpleclient.module.settings.KeyBindSetting;
import simpleclient.module.settings.Setting;
import simpleclient.utils.RendererUtil;

public class Mod {
	
	private String name;
	private String displayName;
	private String description;
	private String info;
	public Category category;
	private int key;
	private boolean enabled;
	private RendererUtil rendererUtil = new RendererUtil();
	
	private List<Setting> settings = new ArrayList<>();
	
	protected MinecraftClient mc = MinecraftClient.getInstance();
	
	public Mod(String name, String description, Category category) {
		this.name = name;
		this.displayName = name;
		this.description = description;
		this.category = category;
		this.info = null;
		
		if (this.category != Category.SETTING) {
			KeyBindSetting keyBindSetting = new KeyBindSetting(name + "-key", 0);
			keyBindSetting.setVisible(false);
			addSetting(keyBindSetting);
		}
	}
	
	public List<Setting> getSettings() {
		return settings;
	}
	
	public void addSetting(Setting setting) {
		settings.add(setting);
	}
	
	public void addSettings(Setting... settings) {
		for (Setting setting : settings) {
			addSetting(setting);
		}
	}
	
	public void toggle() {
		this.enabled = !this.enabled;
		
		if (this.enabled) {
			onEnable();
		} else {
			onDisable();
		}
	}
	
	public void onEnable() {
		this.enabled = true;
	}
	
	public void onDisable() {
		this.enabled = false;
	}
	
	public void onTick() {
		
	}
	
	public RendererUtil getRenderUtils() {
		return this.rendererUtil;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}

	public int getKey() {
		return key;
	}
	
	public char getKeyName() {
		return (char)key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		
		if (enabled) {
			onEnable();
		} else {
			onDisable();
		}
	}

	public Category getCategory() {
		return category;
	}

	public enum Category {
		COMBAT("Combat"), 
		MOVEMENT("Movement"), 
		RENDER("Render"), 
		EXPLOIT("Exploit"),
		WORLD("World"),
		SETTING("Setting"),
        HUDS("HUDS");
		
		public String name;
		
		private Category(String name) {
			this.name = name;
		}
	}
}
