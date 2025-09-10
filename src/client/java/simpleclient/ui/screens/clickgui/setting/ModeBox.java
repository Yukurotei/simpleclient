package simpleclient.ui.screens.clickgui.setting;

import net.minecraft.client.gui.DrawContext;
import simpleclient.module.settings.ModeSetting;
import simpleclient.module.settings.Setting;
import simpleclient.ui.screens.clickgui.ModuleButton;
import simpleclient.utils.StringUtil;

public class ModeBox extends Component{
	
	private ModeSetting modeSet = (ModeSetting)setting;

	public ModeBox(Setting setting, ModuleButton parent, int offset) {
		super(setting, parent, offset);
		this.modeSet = (ModeSetting)setting;
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		context.fill(parent.parent.x, parent.parent.y + parent.offset + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + offset + parent.parent.height, componentColor);
		int textOffset = ((parent.parent.height / 2) - mc.textRenderer.fontHeight / 2);
        StringUtil.shrinkTextToWidthAndDraw(context, parent.parent.width, mc.textRenderer, modeSet.getName() + ": " + modeSet.getMode(), parent.parent.x + textOffset, parent.parent.y + parent.offset + offset + textOffset, -1, true);
		super.render(context, mouseX, mouseY, delta);
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (isHovered(mouseX, mouseY) && button == 0 && parent.extended) {
			modeSet.cycle();
		}
		super.mouseClicked(mouseX, mouseY, button);
	}

}
