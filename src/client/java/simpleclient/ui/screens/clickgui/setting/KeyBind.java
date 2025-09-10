package simpleclient.ui.screens.clickgui.setting;

import java.awt.Color;

import net.minecraft.client.gui.DrawContext;
import simpleclient.module.settings.KeyBindSetting;
import simpleclient.module.settings.Setting;
import simpleclient.ui.screens.clickgui.ModuleButton;
import simpleclient.utils.StringUtil;

public class KeyBind extends Component {
	
	private KeyBindSetting binding = (KeyBindSetting)setting;
	public boolean isBinding = false;
	
	public KeyBind(Setting setting, ModuleButton parent, int offset) {
		super(setting, parent, offset);
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (isHovered(mouseX, mouseY) && button == 0) {
			if (!binding.isVisible() || !parent.extended) return;
			binding.toggle();
			isBinding = true;
		}
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void keyPressed(int key) {
		if ((parent.module.getKey() != binding.getKey()) && isBinding == false) {
			parent.module.setKey(binding.getKey());
		}
		if (!parent.extended || !binding.isVisible()) return;
		if (isBinding == true) {
			parent.module.setKey(key);
			binding.setKey(key);
			isBinding = false;
		}
		if ((binding.getKey() == 256)) {
			parent.module.setKey(0);
			binding.setKey(0);
			isBinding = false;
		}
		if ((binding.getKey() == 259)) {
			parent.module.setKey(0);
			binding.setKey(0);
			isBinding = false;
		}
		super.keyPressed(key);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		if (!binding.isVisible() || !parent.extended) return;
		int clr = 0;
		if (!isBinding) {
			clr = keyBindColor;
		} else {
			clr = keyBindingColor;
		}
		context.fill(parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, clr);
		
		int offsetY = ((parent.parent.height / 2) - mc.textRenderer.fontHeight / 2);
		
		if (!isBinding)
            StringUtil.shrinkTextToWidthAndDraw(context, parent.parent.width, mc.textRenderer, "KeyBind: " + binding.getKeyName(), parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, Color.WHITE.getRGB(), true);
		if (isBinding)
            StringUtil.shrinkTextToWidthAndDraw(context, parent.parent.width, mc.textRenderer, "Binding...", parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, Color.CYAN.getRGB(), true);
		
		super.render(context, mouseX, mouseY, delta);
	}

}
