package simpleclient.ui;

import java.awt.Color;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import simpleclient.utils.RendererUtil;

public class GUI {
	
	protected String name;
	protected RendererUtil renderer = new RendererUtil();
	protected MinecraftClient mc = MinecraftClient.getInstance();
	
	public GUI(String name) {
		this.name = name;
	}
	
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		context.fill(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), new Color(0, 0, 0, 140).getRGB());
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		
	}
	
	public void onKey(int keyCode) {
		
	}
	
	//variables
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

}
