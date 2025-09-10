package simpleclient.module.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import net.minecraft.client.MinecraftClient;
import simpleclient.Client;
import simpleclient.module.Mod;
import simpleclient.module.ModuleManager;
import simpleclient.module.render.BlockESP;
import simpleclient.ui.Color;
import simpleclient.ui.screens.HudElementManager;
import simpleclient.ui.screens.clickgui.ClickGUI;
import simpleclient.ui.screens.clickgui.Frame;

public class SettingManager {
	
	public static File configFolder;
	public static File configFile;
	public static Properties config;
	
	public static MinecraftClient mc = MinecraftClient.getInstance();
	
	public static enum SettingType {
		MODULE, GUIKEY, FRAMEPOSITION, FRIENDLIST, BLOCKESPMAP
	}
	
	public static void prepare(String name) {
		try {
			configFolder = new File(mc.runDirectory + File.separator + "simpleclient");
			configFile = new File(configFolder + File.separator + name + ".xml");
			if (!configFolder.exists()) configFolder.mkdirs();
			if (!configFile.exists()) configFile.createNewFile();
			config = new Properties();
		} catch (Exception ignored) {}
	}
	
	public static void saveSettings(String name, SettingType type) {
		try {
			System.out.println("Saving config " + name + ".");
			prepare(name);
			if (type == SettingType.MODULE) {
				for (Mod module : ModuleManager.INSTANCE.getModules()) {
					for (Setting setting : module.getSettings()) {
						if (setting instanceof BooleanSetting) {
							BooleanSetting boolSet = (BooleanSetting) setting;
							config.setProperty(module.getName() + "-" + setting.getName(), String.valueOf(boolSet.isEnabled()));
						} else if (setting instanceof KeyBindSetting) {
							KeyBindSetting keySet = (KeyBindSetting) setting;
							config.setProperty(module.getName() + "-" + setting.getName(), String.valueOf(keySet.getKey()));
						} else if (setting instanceof ModeSetting) {
							ModeSetting modeSet = (ModeSetting) setting;
							config.setProperty(module.getName() + "-" + setting.getName(), String.valueOf(modeSet.getMode()));
						} else if (setting instanceof NumberSetting) {
							NumberSetting numSet = (NumberSetting) setting;
							config.setProperty(module.getName() + "-" + setting.getName(), String.valueOf(numSet.getValueFloat()));
						}
					}
                    config.setProperty(module.getName() + "#isEnabled", String.valueOf(module.isEnabled()));
				}
                config.storeToXML(new FileOutputStream(configFile), null);
			} else if (type == SettingType.FRAMEPOSITION) {
                for (Frame frame : ClickGUI.getFrames()) {
                    config.setProperty(frame.category.name(), frame.x + "-" + frame.y);
                    config.setProperty(frame.category.name() + "-extended", String.valueOf(frame.extended));
                }
                //huds
                for (Frame frame : HudElementManager.getFrames()) {
                    config.setProperty(frame.correspondingElement.name, frame.x + "-" + frame.y);
                    config.setProperty(frame.correspondingElement.name + "-extended", String.valueOf(frame.extended));
                }
                config.storeToXML(new FileOutputStream(configFile), null);
            }
		} catch (Exception ignored) {}
	}
	
	public static void saveFriends(String name, ArrayList<String> list) {
		try {
			System.out.println("Saving config " + name + ".");
			prepare(name);
			if (!list.isEmpty()) {
				configFolder = new File(mc.runDirectory + File.separator + "simpleclient");
				configFile = new File(configFolder + File.separator + name + ".xml");
				if (!configFolder.exists()) configFolder.mkdirs();
				if (configFile.exists()) configFile.delete();
				prepare(name);
				config.setProperty(name + "-index", String.valueOf(Client.INSTANCE.getFriends().size()));
				for (int i = 0; i < list.size(); i++) {
					config.setProperty(name + "-" + i, String.valueOf(Client.INSTANCE.getFriends().get(i)));
				}
				config.storeToXML(new FileOutputStream(configFile), null);
			}
		} catch (Exception ignored) {}
	}
	
	public static void saveSetting(String name, SettingType type) {
		try {
			//System.out.println("Saving solo config " + name + ".");
			prepare(name);
			if (type == SettingType.GUIKEY) {
				config.setProperty("SC-GUI-KEY", String.valueOf(Client.INSTANCE.getKey()));
				config.storeToXML(new FileOutputStream(configFile), null);
			} else if (type == SettingType.BLOCKESPMAP) {
                StringBuilder entryStringBuilder = new StringBuilder();
                BlockESP module = (BlockESP) ModuleManager.INSTANCE.getModule("BlockESP");
                for (Map.Entry<String, Color> blockEntries : module.blockEntries.entrySet()) {
                    entryStringBuilder.append(blockEntries.getKey()).append(",").append(blockEntries.getValue().r).append("-").append(blockEntries.getValue().g).append("-").append(blockEntries.getValue().b).append("|");
                }
                config.setProperty("SC-BLOCKESP-MAPPINGS", entryStringBuilder.toString());
                config.storeToXML(new FileOutputStream(configFile), null);
            }
		} catch (Exception ignored) {}
	}
	
	public static void loadSettings(String name, SettingType type) {
		try {
			System.out.println("Loading config " + name + ".");
			prepare(name);
			if (type == SettingType.MODULE) {
				config.loadFromXML(new FileInputStream(configFile));
				for (Mod module : ModuleManager.INSTANCE.getModules()) {
					for (Setting setting : module.getSettings()) {
						String value = config.getProperty(module.getName() + "-" + setting.getName(), null);
						if (value == null) continue;
						if (setting instanceof BooleanSetting) {
							((BooleanSetting) setting).setEnabled(Boolean.parseBoolean(value));
						} else if (setting instanceof KeyBindSetting) {
							((KeyBindSetting) setting).setKey(Integer.parseInt(value));
						} else if (setting instanceof ModeSetting) {
							((ModeSetting) setting).setMode(value);
						} else if (setting instanceof NumberSetting) {
							if (((NumberSetting) setting).getMin() <= Double.parseDouble(value) && ((NumberSetting) setting).getMax() >= Double.parseDouble(value)) {
								((NumberSetting) setting).setValue(Double.parseDouble(value));
							}
						}
					}
                    boolean isModuleEnabled = Boolean.parseBoolean(config.getProperty(module.getName() + "#isEnabled"));
                    if (isModuleEnabled) module.setEnabled(true); //Avoid calling onDisable on already disabled module
				}
			} else if (type == SettingType.FRIENDLIST) {
				config.loadFromXML(new FileInputStream(configFile));
				String indexStr = config.getProperty(name + "-index");
				for (int i = 0; i < Integer.parseInt(indexStr); i++) {
					Client.INSTANCE.addFriend(config.getProperty(name + "-" + i));
				}
			} else if (type == SettingType.FRAMEPOSITION) {
                config.loadFromXML(new FileInputStream(configFile));
                for (Frame frame : ClickGUI.getFrames()) {
                    if (config.getProperty(frame.category.name()).split("-")[0] == null) continue; //We save every setting at the same time anyawys
                    int frameX = Integer.parseInt(config.getProperty(frame.category.name()).split("-")[0]);
                    int frameY = Integer.parseInt(config.getProperty(frame.category.name()).split("-")[1]);
                    boolean isExtended = Boolean.parseBoolean(config.getProperty(frame.category.name() + "-extended"));
                    frame.x = frameX;
                    frame.y = frameY;
                    frame.extended = isExtended;
                }
                //huds
                for (Frame frame : HudElementManager.getFrames()) {
                    if (config.getProperty(frame.correspondingElement.name).split("-")[0] == null) continue;
                    int frameX = Integer.parseInt(config.getProperty(frame.correspondingElement.name).split("-")[0]);
                    int frameY = Integer.parseInt(config.getProperty(frame.correspondingElement.name).split("-")[1]);
                    boolean isExtended = Boolean.parseBoolean(config.getProperty(frame.correspondingElement.name + "-extended"));
                    frame.x = frameX;
                    frame.y = frameY;
                    frame.extended = isExtended;
                }
            }
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void loadSetting(String name, SettingType type) {
		try {
			//System.out.println("Loading solo config " + name + ".");
			prepare(name);
			if (type == SettingType.GUIKEY) {
				config.loadFromXML(new FileInputStream(configFile));
				String value = config.getProperty("SC-GUI-KEY", null);
				Client.INSTANCE.setKey(Integer.parseInt(value));
			} else if (type == SettingType.BLOCKESPMAP) {
                config.loadFromXML(new FileInputStream(configFile));
                for (String blockEntryString : config.getProperty("SC-BLOCKESP-MAPPINGS").split("\\|")) {
                    String blockName = blockEntryString.split(",")[0];
                    String colorString = blockEntryString.split(",")[1];
                    Color blockColor = new Color(Integer.parseInt(colorString.split("-")[0]), Integer.parseInt(colorString.split("-")[1]), Integer.parseInt(colorString.split("-")[2]));
                    BlockESP module = (BlockESP) ModuleManager.INSTANCE.getModule("BlockESP");
                    module.blockEntries.put(blockName, blockColor);
                }
            }
		} catch (Exception e) {e.printStackTrace();}
	}
}
