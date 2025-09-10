package simpleclient.ui.screens;

import net.minecraft.client.gui.DrawContext;
import simpleclient.Client;
import simpleclient.SimpleClient;
import simpleclient.event.events.MouseLeftClickEvent;
import simpleclient.event.listeners.MouseLeftClickListener;
import simpleclient.ui.GUI;
import simpleclient.ui.Hud;
import simpleclient.ui.screens.clickgui.Frame;
import simpleclient.ui.screens.clickgui.huds.TargetHud;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HudElementManager extends GUI implements MouseLeftClickListener {

    private static List<Frame> frames;

    public HudElementManager() {
        super("HudElements");

        frames = new ArrayList<>();

        //Add huds here
        Hud.addElement(new TargetHud(null));

        //Hud elements
        int hudOffset = 20;
        for (HudElement element : Hud.getHudElements()) {
            Client.INSTANCE.logger.info(element.displayName);
            Frame newFrame = new Frame(element.displayName, element, hudOffset, 40, 70, 10);
            frames.add(newFrame);
            if (element.parentFrame == null) element.parentFrame = newFrame;
        }
    }

    public static List<Frame> getFrames() {
        return frames;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //super.render(context, mouseX, mouseY, delta);
        //context.fill(0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), new Color(0, 0, 0, 140).getRGB());
        //simpleclient.Client.getInstance();
        //String guiText = "SimpleClient v" + SimpleClient.VERSION;
        //context.drawText(mc.textRenderer, guiText, mc.getWindow().getScaledWidth() - mc.textRenderer.getWidth(guiText) - 10, mc.getWindow().getScaledHeight() - 15, new Color(67, 73, 196, 255).getRGB(), true);
        if (Client.INSTANCE.guiOpened) {
            for (Frame frame : frames) {
                frame.render(context, mouseX, mouseY, delta);
                frame.updatePosition(mouseX, mouseY);
            }
        }
        for (HudElement element : Hud.getHudElements()) {
            if (element.parentFrame.extended) element.render(context);
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

    }
}
