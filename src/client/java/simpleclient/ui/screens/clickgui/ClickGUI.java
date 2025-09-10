package simpleclient.ui.screens.clickgui;

import java.awt.Color;
import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

import net.minecraft.client.gui.DrawContext;
import simpleclient.Client;
import simpleclient.SimpleClient;
import simpleclient.event.events.MouseLeftClickEvent;
import simpleclient.event.listeners.MouseLeftClickListener;
import simpleclient.module.Mod.Category;
import simpleclient.ui.GUI;
import simpleclient.ui.Hud;

public class ClickGUI extends GUI implements MouseLeftClickListener { //extends Screen 
	
	private static List<Frame> frames;
	
	public ClickGUI() {
		super("ClickGUI");
		//super(Text.literal("SimpleClient"));
		
		frames = new ArrayList<>();
		
		int offset = 20;
		for (Category category : Category.values()) {
            if (Objects.equals(category.name, "HUDS")) return;
			frames.add(new Frame(category, offset, 20, 70, 10));
			offset += 80;
		}
	}

    public static List<Frame> getFrames() {
        return frames;
    }

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		//context.fill(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), new Color(0, 0, 0, 140).getRGB());
		simpleclient.Client.getInstance();
		String guiText = "SimpleClient v" + SimpleClient.VERSION;
		context.drawText(mc.textRenderer, guiText, mc.getWindow().getScaledWidth() - mc.textRenderer.getWidth(guiText) - 10, mc.getWindow().getScaledHeight() - 15, new Color(67, 73, 196, 255).getRGB(), true);
		for (Frame frame : frames) {
			frame.render(context, mouseX, mouseY, delta);
			frame.updatePosition(mouseX, mouseY);
		}
		//super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		for (Frame frame : frames) {
			frame.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		for (Frame frame : frames) {
			frame.mouseReleased(mouseX, mouseY, button);
		}
	}
	
	@Override
	public void onKey(int keyCode) {
		for (Frame frame : frames) {
			frame.keyPressed(keyCode);
		}
	}



	@Override
	public void OnMouseLeftClick(MouseLeftClickEvent event) {
		if (Client.INSTANCE.guiOpened) {
			event.SetCancelled(true);
		}
		
	}
	
	//EXTRA
	/*
	@Override
	public boolean shouldPause() {
		return false;
	}
	*/
}
