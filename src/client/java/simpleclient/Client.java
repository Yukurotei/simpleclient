package simpleclient;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import simpleclient.module.Mod;
import simpleclient.module.ModuleManager;
import simpleclient.utils.ChatUtil;

public class Client implements ModInitializer {

	public static final Client INSTANCE = new Client();
	public Logger logger = LogManager.getLogger(Client.class);
	
	public static MinecraftClient mc = MinecraftClient.getInstance();
	
	public boolean guiOpened = false, mousePressedFired = false, mouseReleasedFired = false;
	private int guiKey = GLFW.GLFW_KEY_RIGHT_SHIFT;
	public double mouseX = 0, mouseY = 0;
	public float delta = 1;
	//Special module variables
	public ArrayList<String> friends = new ArrayList<>();
	public boolean extendedReach = false, rainbowHud = false, showKeyBind = false, specialHud = false, renderObjects = true, displayModules = true;
	public float reach = 0f;
	public simpleclient.ui.Color rainbowHudColor = null;
    public String testValue = "test";
	public enum HudDisplayMode {
		ASCORDS,
		INVIS
	}
	public enum HudDisplaySide {
		LEFT,
		RIGHT
	}
	public HudDisplayMode specialHudDisplayMode = HudDisplayMode.ASCORDS;
	public HudDisplaySide specialHudDisplaySide = HudDisplaySide.LEFT;
	public int specialHudCordsColor = 0;
	public int specialHudDirectionsColor = 0;
	
	public static SimpleClient instance;
	
	
	@Override
	public void onInitialize() {
		instance = new SimpleClient();
		instance.Initialize();
	}
	
	public static SimpleClient getInstance() {
		return instance;
	}
	
	public void onKeyPress(int key, int action) {
		renderObjects = true;
		if (action == GLFW.GLFW_PRESS) {
			for (Mod module : ModuleManager.INSTANCE.getModules()) {
				if (key == module.getKey()) {
					if (mc.currentScreen == null && !guiOpened) {
						module.toggle();
					}
				}
			}
			
			if (key == guiKey) {
				if (guiOpened) {
					guiOpened = false;
					if(!mc.mouse.isCursorLocked()) {
						mc.mouse.lockCursor();
					}else {
						mc.mouse.unlockCursor();
					}
					//mc.setScreen(null);
				} else {
					guiOpened = true;
					if(mc.mouse.isCursorLocked()) {
						mc.mouse.unlockCursor();
					}else {
						mc.mouse.lockCursor();
					}
					//mc.setScreen(ClickGUI.INSTANCE);
				}
			}
			
			if (key == GLFW.GLFW_KEY_F2) {
				ChatUtil.insertMessage("Please note that taking screen shots might have risks of exposing yourself using a hack client!", ChatUtil.MessageType.WARNING);
			}
		}
	}
	
	public void onTick() {
		if (mc != null) {
			if (mc.player != null) {
				for (Mod module : ModuleManager.INSTANCE.getEnabledModules()) {
					module.onTick();
				}
			}
		}
	}
	
	public int getKey() {
		return guiKey;
	}
	
	public void setKey(int key) {
		guiKey = key;
	}
	
	public ArrayList<String> getFriends() {
		return friends;
	}
	
	public void addFriend(String name) {
		friends.add(name.toLowerCase());
	}
	
	public boolean removeFriend(String name) {
		String nameToRemove = name.toLowerCase();
		boolean foundAndRemoved = false;
		for (String friendName : friends) {
			if (friendName.toLowerCase().equals(nameToRemove)) {
				friends.remove(nameToRemove);
				foundAndRemoved = true;
				break;
			}
		}
		return foundAndRemoved;
	}
	
	public boolean doesFriendExist(String name) {
		String nameToCompare = name.toLowerCase();
		boolean foundAndCompared = false;
		for (String friendName : friends) {
			if (friendName.toLowerCase().equals(nameToCompare)) {
				foundAndCompared = true;
				break;
			}
		}
		return foundAndCompared;
	}

}
