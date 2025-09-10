package simpleclient.ui.screens.clickgui.setting;

import java.awt.Color;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.DrawContext;
import simpleclient.module.settings.InputSetting;
import simpleclient.module.settings.Setting;
import simpleclient.ui.screens.clickgui.ModuleButton;
import simpleclient.utils.StringUtil;

public class InputBox extends Component {

	private InputSetting inputSet = (InputSetting) setting;
	public boolean isTyping = false;
	
	public InputBox(Setting setting, ModuleButton parent, int offset) {
		super(setting, parent, offset);
		this.inputSet = (InputSetting) setting;
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (isHovered(mouseX, mouseY) && button == 0 && parent.extended) {
			inputSet.toggle();
			isTyping = true;
		}
		super.mouseClicked(mouseX, mouseY, button);
	}
	//TODO: add letter awaiting system, combine keypressed and keyreleased
	@Override
	public void keyPressed(int key) {
		if (isTyping) {
			if (key == GLFW.GLFW_KEY_ENTER) {
				inputSet.setDoneTyping(true);
				isTyping = false;
			} else if (key == GLFW.GLFW_KEY_BACKSPACE) {
				if (!inputSet.getContent().isEmpty()) {
					String ctnt = inputSet.getContent();
					ctnt = ctnt.substring(0, ctnt.length() - 1);
					inputSet.setContent(ctnt);
				}
			} else {
				char letter = (char) key;
				inputSet.addLetterToContent(letter);
			}
		}
		super.keyPressed(key);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		int clr = 0;
		if (isTyping) {
			clr = inputTypingColor;
		} else {
			clr = inputColor;
		}
		
		context.fill(parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, clr);
		int offsetY = ((parent.parent.height / 2) - mc.textRenderer.fontHeight / 2);
		if (!isTyping) StringUtil.shrinkTextToWidthAndDraw(context, parent.parent.width, mc.textRenderer, "" + inputSet.getPlaceHolder(), parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, Color.WHITE.getRGB(), true);
		if (isTyping) StringUtil.shrinkTextToWidthAndDraw(context, parent.parent.width, mc.textRenderer, inputSet.getContent(), parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, Color.GREEN.getRGB(), true);
		super.render(context, mouseX, mouseY, delta);
	}
}
