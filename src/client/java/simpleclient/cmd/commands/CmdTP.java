package simpleclient.cmd.commands;

import simpleclient.cmd.Command;
import simpleclient.cmd.InvalidSyntaxException;
import simpleclient.utils.BasicUtil;
import simpleclient.utils.ChatUtil;

import java.util.List;

public class CmdTP extends Command {

	public CmdTP() {
		super("tp", "Teleports the player certain blocks away (Vanilla only)", "[x] [y] [z]");
	}

	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		if (parameters.length != 3)
			throw new InvalidSyntaxException(this);
	
			if (BasicUtil.isNumeric(parameters[0]) && BasicUtil.isNumeric(parameters[1]) && BasicUtil.isNumeric(parameters[2])) {
				mc.player.setPosition(Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1]), Double.parseDouble(parameters[2]));
				mc.player.updatePosition(Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1]), Double.parseDouble(parameters[2]));
				ChatUtil.insertMessage("Attempting to teleport player to " + parameters[0] + ", " + parameters[1] + ", " + parameters[2], ChatUtil.MessageType.NORMAL);
			} else {
				ChatUtil.insertMessage("X Y Z must be integers", ChatUtil.MessageType.WARNING);
				return;
			}
	}

    @Override
    public String[] getAutocorrect(List<String> parameters) {
		return new String[] {"0 0 0"};
	}
}