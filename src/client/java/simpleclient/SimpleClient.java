package simpleclient;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import simpleclient.cmd.CommandManager;
import simpleclient.event.EventManager;
import simpleclient.interfaces.IMinecraftClient;
import simpleclient.module.ModuleManager;
import simpleclient.module.settings.SettingManager;
import simpleclient.ui.GuiManager;
import simpleclient.utils.RendererUtil;

public class SimpleClient implements ClientModInitializer {
	
	public static final String NAME = "simpleclient";
	public static final String VERSION = "2.0.0 GUI Overhaul";
	public static final String PREFIX = ".sc";
	
	public static MinecraftClient MC;
	public static IMinecraftClient IMC;
	
	// Systems
	public RendererUtil rendererUtil;
	public EventManager eventManager;
	public ModuleManager moduleManager;
	public CommandManager commandManager;
	public GuiManager guiManager;
	public SettingManager settingManager;
	//public HudManager hudManager;
	
	@Override
	public void onInitializeClient() {
	}
	
	public void Initialize() {
		MC = MinecraftClient.getInstance();
		
		System.out.println("[SimpleClient] Starting Client");
		rendererUtil = new RendererUtil();
		System.out.println("[SimpleClient] Initializing Events");
		eventManager = new EventManager();
		System.out.println("[SimpleClient] Initializing Modules");
		moduleManager = new ModuleManager();
		System.out.println("[SimpleClient] Initializing Commands");
		commandManager = new CommandManager();
		System.out.println("[SimpleClient] Initializing GUI");
		//hudManager = new HudManager();
		guiManager = GuiManager.INSTANCE;
		System.out.println("[SimpleClient] Loading Settings");
		settingManager = new SettingManager();
		System.out.println("[SimpleClient] Client has started");
		
		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						SettingManager.loadSettings("modules", SettingManager.SettingType.MODULE);
                        SettingManager.loadSettings("FramePos", SettingManager.SettingType.FRAMEPOSITION);
						SettingManager.loadSetting("ClickGUI", SettingManager.SettingType.GUIKEY);
                        SettingManager.loadSetting("BlockESPMaps", SettingManager.SettingType.BLOCKESPMAP);
						SettingManager.loadSettings("friends", SettingManager.SettingType.FRIENDLIST);
						cancel();
					}
				},
				5000
		);
		
	}
	
	public void endClient() {
		SettingManager.saveSettings("modules", SettingManager.SettingType.MODULE);
        SettingManager.saveSettings("FramePos", SettingManager.SettingType.FRAMEPOSITION);
		SettingManager.saveSetting("ClickGUI", SettingManager.SettingType.GUIKEY);
        SettingManager.saveSetting("BlockESPMaps", SettingManager.SettingType.BLOCKESPMAP);
		SettingManager.saveFriends("friends", Client.INSTANCE.getFriends());
		System.out.println("[SimpleClient] Shutting Down...");
	}
}