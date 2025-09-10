package simpleclient.ui.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import simpleclient.ui.screens.clickgui.Frame;

public class HudElement {

    public Frame parentFrame;
    public String name;
    public String displayName;
    public String description;
    public int originX = 0;
    public int originY = 0;

    protected MinecraftClient mc = MinecraftClient.getInstance();

    public HudElement(Frame parentFrame, String name, String displayName, String description) {
        this.parentFrame = parentFrame;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
    }

    public void render(DrawContext context) {
        //Mostly x and y update stuff
        originX = parentFrame.x;
        originY = parentFrame.y + parentFrame.height;
    }
}
