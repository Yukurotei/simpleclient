package simpleclient.cmd.commands;

import simpleclient.cmd.Command;
import simpleclient.cmd.InvalidSyntaxException;
import simpleclient.module.ModuleManager;
import simpleclient.module.render.EntityESP;
import simpleclient.utils.ChatUtil;

import java.util.List;

public class CmdEntityESP extends Command {
	
	public CmdEntityESP() {
		super("entityesp", "Does what it says", "[toggle] [value]");
	}
	
	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		if (parameters.length != 2) throw new InvalidSyntaxException(this);
		
		EntityESP module = (EntityESP) ModuleManager.INSTANCE.getModule("EntityESP");
		if (module.equals(null)) {
			ChatUtil.insertMessage("Unhandled Exception: module is null", ChatUtil.MessageType.DANGER);
			return;
		}
		
		
		switch (parameters[0]) {
		case "toggle":
			String state = parameters[1].toLowerCase();
			if (state.equals("on")) {
				module.setEnabled(true);;
				ChatUtil.insertMessage("EntityESP toggled ON", ChatUtil.MessageType.NORMAL);
			} else if (state.equals("off")) {
				module.setEnabled(false);
				ChatUtil.insertMessage("EntityESP toggled OFF", ChatUtil.MessageType.NORMAL);
			} else {
				ChatUtil.insertMessage("Invalid Value. [ON/OFF]", ChatUtil.MessageType.NORMAL);
			}
			break;
		default:
			throw new InvalidSyntaxException(this);
		}
	}
	
	@Override
	public String[] getAutocorrect(List<String> parameters) {
        String previousParameter = parameters.get(parameters.size() - 1);
		switch (previousParameter) {
		case "toggle":
			return new String[] { "on", "off" };
		default:
			return new String[] { "toggle" };
		}
	}

}
