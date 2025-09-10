package simpleclient.ui.screens.clickgui.setting;

import java.awt.Color;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import simpleclient.Client;
import simpleclient.module.Mod.Category;
import simpleclient.module.settings.Setting;
import simpleclient.ui.screens.clickgui.ModuleButton;

public class Component {
	
	public Setting setting;
	public ModuleButton parent;
	public int offset;
	public int offsetVal;
	
	protected MinecraftClient mc = MinecraftClient.getInstance();
	
	protected int componentColor = new Color(107, 182, 209, 10).getRGB();
	protected int sliderColor = new Color(39, 125, 186).getRGB();
	protected int keyBindColor = new Color(60, 230, 179, 50).getRGB();
	protected int keyBindingColor = new Color(0, 255, 179).getRGB();
	protected int inputColor = new Color(140, 3, 252, 50).getRGB();
	protected int inputTypingColor = new Color(252, 3, 244).getRGB();
	
	public Component(Setting setting, ModuleButton parent, int offset) {
		this.setting = setting;
		this.parent = parent;
		this.offset = offset;
		this.offsetVal = offset;
	}
	
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		if (this.parent.module.getCategory() != Category.SETTING) {
			if (!Client.INSTANCE.showKeyBind) {
				this.offset = this.offsetVal - parent.parent.height;
				this.parent.parent.updateButtons();
			} else {
				this.offset = this.offsetVal;
				this.parent.parent.updateButtons();
			}
		} else {
			this.offset = this.offsetVal;
			this.parent.parent.updateButtons();
		}
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX > parent.parent.x && mouseX < parent.parent.x + parent.parent.width && mouseY > parent.parent.y + parent.offset + offset && mouseY < parent.parent.y + parent.offset + offset + parent.parent.height;
	}

	public void keyPressed(int key) {
		
	}
}
