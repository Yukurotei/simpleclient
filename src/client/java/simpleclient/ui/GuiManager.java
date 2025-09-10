package simpleclient.ui;

import java.util.ArrayList;
import java.util.List;

import simpleclient.ui.screens.HudElementManager;
import simpleclient.ui.screens.clickgui.ClickGUI;

public class GuiManager {
	
	public static final GuiManager INSTANCE = new GuiManager();
	public List<GUI> GUIs = new ArrayList<>();
	
	public GuiManager() {
		addGUIs();
	}
	
	public GUI getGUI(String name) {
		for (GUI gui : GUIs) {
			if (gui.getName().equals(name)) {
				return gui;
			}
		}
		return null;
	}
	
	private void addGUIs() {
        GUIs.add(new HudElementManager());
		GUIs.add(new ClickGUI());
	}

}
