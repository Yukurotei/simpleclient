package simpleclient.ui.screens.clickgui.setting;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.minecraft.client.gui.DrawContext;
import simpleclient.module.settings.NumberSetting;
import simpleclient.module.settings.Setting;
import simpleclient.ui.screens.clickgui.ModuleButton;
import simpleclient.utils.StringUtil;

public class Slider extends Component{
	
	public NumberSetting numSet = (NumberSetting)setting;
	
	private boolean sliding = false;

	public Slider(Setting setting, ModuleButton parent, int offset) {
		super(setting, parent, offset);
		this.numSet = (NumberSetting)setting;
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		context.fill(parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, componentColor);
		
		double diff = Math.min(parent.parent.width, Math.max(0, mouseX - parent.parent.x));
		int renderWidth = (int) (parent.parent.width * (numSet.getValue() - numSet.getMin()) / (numSet.getMax() - numSet.getMin()));
		
		context.fill(parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + renderWidth, parent.parent.y + parent.offset + offset + parent.parent.height, sliderColor);
		
		if (sliding) {
			if (diff == 0) {
				numSet.setValue(numSet.getMin());
			} else {
				numSet.setValue(roundToPlace((diff / parent.parent.width) * (numSet.getMax() - numSet.getMin()) + numSet.getMin(), 2));
			}
		}
		
		int textOffset = ((parent.parent.height / 2) - mc.textRenderer.fontHeight / 2);
        StringUtil.shrinkTextToWidthAndDraw(context, parent.parent.width, mc.textRenderer, numSet.getName() + ": " + roundToPlace(numSet.getValue(), 2), parent.parent.x + textOffset, parent.parent.y + parent.offset + offset + textOffset, -1, true);
		super.render(context, mouseX, mouseY, delta);
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (isHovered(mouseX, mouseY) && parent.extended) {
			sliding = true;
		}
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		sliding = false;
		super.mouseReleased(mouseX, mouseY, button);
	}
	
	private double roundToPlace(double value, int place) {
		if (place < 0) {
			return value;
		}
		
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(place, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
