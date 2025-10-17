package simpleclient.ui.screens.clickgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import simpleclient.module.Mod;
import simpleclient.module.Mod.Category;
import simpleclient.module.ModuleManager;
import simpleclient.ui.screens.HudElement;
import simpleclient.ui.screens.clickgui.setting.Component;
import simpleclient.utils.StringUtil;

public class Frame {
	
	public int x, y, width, height, dragX, dragY;
	public Category category;
    public String name;
	
	public boolean dragging, extended;
	
	private List<ModuleButton> buttons;
    public HudElement correspondingElement;
	
	protected MinecraftClient mc = MinecraftClient.getInstance();

	public Frame(Category category, int x, int y, int width, int height) {
		this.category = category;
        this.name = category.name;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.dragging = false;
		this.extended = false;
		
		buttons = new ArrayList<>();
		
		int offset = height;
		for (Mod mod : ModuleManager.INSTANCE.getModulesInCategory(category)) {
			buttons.add(new ModuleButton(mod, this, offset));
			offset += height;
		}
	}

    public Frame(String name, HudElement correspondingElement, int x, int y, int width, int height) {
        this.category = Category.HUDS;
        this.correspondingElement = correspondingElement;
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dragging = false;
        this.extended = false;

        buttons = new ArrayList<>();

        /*
        int offset = height;
        for (Mod mod : ModuleManager.INSTANCE.getModulesInCategory(category)) {
            buttons.add(new ModuleButton(mod, this, offset));
            offset += height;
        }
         */
    }
	
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.category == Category.HUDS) {
            context.fill(x, y, x + width, y + height, new Color(67, 73, 196, 200).getRGB());
            int offset = ((height / 2) - mc.textRenderer.fontHeight / 2);
            StringUtil.shrinkTextToWidthAndDraw(context, width, mc.textRenderer, name, x + offset, y + offset, -1, true);
            context.drawText(mc.textRenderer, extended ? "-" : "+", x + width - offset - mc.textRenderer.getWidth("+"), y + offset, -1, true);

            /*
            if (extended) {
                for (ModuleButton button : buttons) {
                    button.render(context, mouseX, mouseY, delta);
                }
            }
             */
        } else {
            context.fill(x, y, x + width, y + height, new Color(67, 73, 196, 200).getRGB());
            int offset = ((height / 2) - mc.textRenderer.fontHeight / 2);
            StringUtil.shrinkTextToWidthAndDraw(context, width, mc.textRenderer, category.name, x + offset, y + offset, -1, true);
            context.drawText(mc.textRenderer, extended ? "-" : "+", x + width - offset - mc.textRenderer.getWidth("+"), y + offset, -1, true);

            if (extended) {
                for (ModuleButton button : buttons) {
                    button.render(context, mouseX, mouseY, delta);
                }
            }
        }
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (isHovered(mouseX, mouseY)) {
			if (button == 0) {
				dragging = true;
				dragX = (int) (mouseX - x);
				dragY = (int) (mouseY - y);
			} else if (button == 1) {
				extended = !extended;
			}
		}
		if (extended) {
			for (ModuleButton mb : buttons) {
				mb.mouseClicked(mouseX, mouseY, button);
			}
		}
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		if (button == 0 && dragging) {
			dragging = false;
		}
		
		for (ModuleButton mb : buttons) {
			mb.mouseReleased(mouseX, mouseY, button);
		}
	}
	
	public void keyPressed(int key) {
		for (ModuleButton mb : buttons) {
			mb.keyPressed(key);
		}
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
	}
	
    public void updatePosition(double mouseX, double mouseY) {
        if (dragging) {
            x = (int) (mouseX - dragX);
            y = (int) (mouseY - dragY);
        }
    }

    public int getTotalHeight() {
        if (!extended) {
            return height;
        }
        int totalHeight = height;
        for (ModuleButton button : buttons) {
            totalHeight += height;
            if (button.extended) {
                for (Component component : button.components) {
                    if (component.setting.isVisible()) {
                        totalHeight += height;
                    }
                }
            }
        }
        return totalHeight;
    }

	public void updateButtons() {
		int offset = height;
		
		for (ModuleButton button : buttons) {
			button.offset = offset;
			offset += height;
					
			if (button.extended) {
				for (Component component : button.components) {
					if (component.setting.isVisible()) offset += height;
				}
			}
		}
	}
}
