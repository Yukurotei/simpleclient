package simpleclient.utils;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChatUtil extends Utils {
	
	public enum MessageType {
		NORMAL,
		WARNING,
		DANGER
	}
	
	/**
	 * Inserts a message into the Minecraft Chat.
	 * @param message The message to be printed.
	 * @param type The type of the message, could be normal, warning or danger
	 */
	public static void insertMessage(String message, MessageType type) {
		if (mc.inGameHud == null) return;
		if (type == MessageType.NORMAL) {
			mc.inGameHud.getChatHud().addMessage(Text.of(Formatting.AQUA + "[" + Formatting.BLUE + "SimpleClient" + Formatting.AQUA + "]" + Formatting.RESET + " " + message));
		} else if (type == MessageType.WARNING) {
			mc.inGameHud.getChatHud().addMessage(Text.of(Formatting.AQUA + "[" + Formatting.BLUE + "SimpleClient" + Formatting.AQUA + "][" + Formatting.YELLOW + "WARNING" + Formatting.AQUA + "] " + Formatting.YELLOW + message + Formatting.RESET));
		} else if (type == MessageType.DANGER) {
			mc.inGameHud.getChatHud().addMessage(Text.of(Formatting.AQUA + "[" + Formatting.BLUE + "SimpleClient" + Formatting.AQUA + "][" + Formatting.RED + "DANGER" + Formatting.AQUA + "] " + message + Formatting.RESET));
		}
	}
	
}
