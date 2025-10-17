package simpleclient.ui.screens.clickgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.DrawContext;
import simpleclient.Client;
import simpleclient.module.Mod;
import simpleclient.module.settings.BooleanSetting;
import simpleclient.module.settings.InputSetting;
import simpleclient.module.settings.KeyBindSetting;
import simpleclient.module.settings.ModeSetting;
import simpleclient.module.settings.NumberSetting;
import simpleclient.module.settings.Setting;
import simpleclient.ui.screens.clickgui.setting.CheckBox;
import simpleclient.ui.screens.clickgui.setting.Component;
import simpleclient.ui.screens.clickgui.setting.InputBox;
import simpleclient.ui.screens.clickgui.setting.KeyBind;
import simpleclient.ui.screens.clickgui.setting.ModeBox;
import simpleclient.ui.screens.clickgui.setting.Slider;
import simpleclient.utils.StringUtil;

public class ModuleButton {
	
	public Mod module;
	public Frame parent;
	public int offset;
	public List<Component> components;
	public boolean extended;
    public long displayDescriptionAfterMillis = 670;

    private long lastUnhovered = 0;

	public ModuleButton(Mod module, Frame parent, int offset) {
		this.module = module;
		this.parent = parent;
		this.offset = offset;
		this.extended = false;
		this.components = new ArrayList<>();
		
		int setOffset = parent.height;
		for (Setting setting : module.getSettings()) {
			if (setting instanceof BooleanSetting) {
				components.add(new CheckBox(setting, this, setOffset));
			} else if (setting instanceof ModeSetting) {
				components.add(new ModeBox(setting, this, setOffset));
			} else if (setting instanceof NumberSetting) {
				components.add(new Slider(setting, this, setOffset));
			} else if (setting instanceof KeyBindSetting) {
				components.add(new KeyBind(setting, this, setOffset));
			} else if (setting instanceof InputSetting) {
				components.add(new InputBox(setting, this, setOffset));
			}
			setOffset += parent.height;
		}
	}
	
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		context.fill(parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + parent.height, new Color(39, 125, 186, 160).getRGB());
		if (isHovered(mouseX, mouseY)) {
			context.fill(parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + parent.height, new Color(0, 0, 0, 50).getRGB());
            long elapsed = System.currentTimeMillis() - lastUnhovered;
            if (elapsed >= displayDescriptionAfterMillis) { //0.67 sec
                //Check if description overlaps with another frame
                boolean reverse = false;
                for (Frame frame : ClickGUI.getFrames()) {
                    if (frame != parent && (parent.x + parent.width) < (frame.x + frame.width) && (parent.x + parent.width + parent.mc.textRenderer.getWidth(module.getDescription())) > frame.x && (parent.y + offset) < (frame.y + frame.getTotalHeight()) && (parent.y + offset + parent.mc.textRenderer.fontHeight) > frame.y) {
                        reverse = true;
                        break;
                    }
                }
                if (reverse) {
                    context.fill(parent.x, parent.y + offset, parent.x - parent.mc.textRenderer.getWidth(module.getDescription()), parent.y + offset + parent.mc.textRenderer.fontHeight, new Color(0, 0, 0, 50).getRGB());
                    StringUtil.shrinkTextToWidthAndDraw(context, parent.mc.textRenderer.getWidth(module.getDescription()), parent.mc.textRenderer, module.getDescription(), parent.x - parent.mc.textRenderer.getWidth(module.getDescription()), parent.y + offset, module.isEnabled() ? Color.cyan.getRGB() : -1, true);
                } else {
                    context.fill(parent.x + parent.width, parent.y + offset, parent.x + parent.width + parent.mc.textRenderer.getWidth(module.getDescription()), parent.y + offset + parent.mc.textRenderer.fontHeight, new Color(0, 0, 0, 50).getRGB());
                    StringUtil.shrinkTextToWidthAndDraw(context, parent.mc.textRenderer.getWidth(module.getDescription()), parent.mc.textRenderer, module.getDescription(), parent.x + parent.width, parent.y + offset, module.isEnabled() ? Color.cyan.getRGB() : -1, true);
                }
            }
		} else {
            lastUnhovered = System.currentTimeMillis();
        }
		int textOffset = ((parent.height / 2) - parent.mc.textRenderer.fontHeight / 2);
        StringUtil.shrinkTextToWidthAndDraw(context, parent.width, parent.mc.textRenderer, module.getName(), parent.x + textOffset, parent.y + offset + textOffset, module.isEnabled() ? Color.cyan.getRGB() : -1, true);
		if (!components.isEmpty()) {
			boolean doExtendableCheck = false;
			for (Component component : components) {
				if (component instanceof KeyBind) {
					doExtendableCheck = true;
				}
			}
			if (!doExtendableCheck) {
				context.drawText(parent.mc.textRenderer, extended ? "-" : "+", parent.x + parent.width - textOffset - parent.mc.textRenderer.getWidth("+"), parent.y + offset + textOffset, module.isEnabled() ? Color.cyan.getRGB() : -1, true);
			}
			if (!(components.size() == 1 && !Client.INSTANCE.showKeyBind) && doExtendableCheck) {
				context.drawText(parent.mc.textRenderer, extended ? "-" : "+", parent.x + parent.width - textOffset - parent.mc.textRenderer.getWidth("+"), parent.y + offset + textOffset, module.isEnabled() ? Color.cyan.getRGB() : -1, true);
			}
		}
		
		if (extended) {
			for (Component component : components) {
				parent.updateButtons();
				component.render(context, mouseX, mouseY, delta);
			}
		}
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
			if (isHovered(mouseX, mouseY)) {
				if (button == 0) {
					module.toggle();
				} else if (button == 1){
					extended = !extended;
					parent.updateButtons();
				}
			}
			
			for (Component component : components) {
				component.mouseClicked(mouseX, mouseY, button);
			}
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		for (Component component : components) {
			component.mouseReleased(mouseX, mouseY, button);
		}
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX > parent.x && mouseX < parent.x + parent.width && mouseY > parent.y + offset && mouseY < parent.y + offset + parent.height;
	}

	public void keyPressed(int key) {
		for (Component component : components) {
            component.keyPressed(key);
        }
	}
}
