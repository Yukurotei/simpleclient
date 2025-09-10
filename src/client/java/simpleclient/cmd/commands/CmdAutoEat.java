package simpleclient.cmd.commands;

import simpleclient.cmd.Command;
import simpleclient.cmd.InvalidSyntaxException;
import simpleclient.module.ModuleManager;
import simpleclient.module.exploit.AutoEat;
import simpleclient.utils.ChatUtil;

import java.util.List;

public class CmdAutoEat extends Command {

	public CmdAutoEat() {
		super("autoeat", "Automatically eats when the player is hungry.", "[toggle/set] [value]");
	}

	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		if (parameters.length != 2)
			throw new InvalidSyntaxException(this);

		AutoEat module = (AutoEat) ModuleManager.INSTANCE.getModule("AutoEat");
		if (module.equals(null)) {
			ChatUtil.insertMessage("Unhandled Exception: module is null", ChatUtil.MessageType.DANGER);
			return;
		}

		switch (parameters[0]) {
		case "toggle":
			String state = parameters[1].toLowerCase();
			if (state.equals("on")) {
				module.setEnabled(true);
				ChatUtil.insertMessage("AutoEat toggled ON", ChatUtil.MessageType.NORMAL);
			} else if (state.equals("off")) {
				module.setEnabled(false);
				ChatUtil.insertMessage("AutoEat toggled OFF", ChatUtil.MessageType.NORMAL);
			} else {
				ChatUtil.insertMessage("Invalid value. [ON/OFF]", ChatUtil.MessageType.WARNING);
			}
			break;
		case "set":
			String setting = parameters[1].toLowerCase();
			if (setting.isEmpty()) {
				ChatUtil.insertMessage("Please enter the number of food level to set to.", ChatUtil.MessageType.WARNING);
			} else {
				module.setHunger((int) Math.min(Double.parseDouble(setting) * 2, 20));
				ChatUtil.insertMessage("AutoEat hunger set to " + setting + " food level.", ChatUtil.MessageType.NORMAL);
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
		case "set":
			return new String[] { "1", "2", "4", "6", "8", "10" };
		default:
			return new String[] { "toggle", "set" };
		}
	}
}