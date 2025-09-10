package simpleclient.cmd;

import net.minecraft.util.Formatting;
import simpleclient.utils.ChatUtil;

public class InvalidSyntaxException extends CommandException {
	private static final long serialVersionUID = 1L;
	
	public InvalidSyntaxException(Command cmd) {
		super(cmd);
	}

	@Override
	public void PrintToChat() {
		ChatUtil.insertMessage("Invalid Usage: " + Formatting.AQUA + ".sc " + cmd.getName() + " " + cmd.getSyntax() + Formatting.RESET, ChatUtil.MessageType.WARNING);
	}
}