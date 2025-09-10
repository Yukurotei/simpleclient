package simpleclient.cmd.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.util.Formatting;
import simpleclient.Client;
import simpleclient.cmd.Command;
import simpleclient.cmd.CommandManager;
import simpleclient.module.Mod;
import simpleclient.module.ModuleManager;
import simpleclient.utils.ChatUtil;

public class CmdHelp extends Command {

	int indexesPerPage = 5;

	public CmdHelp() {
		super("help", "Shows the avaiable commands.", "[page, module]");
	}

	@Override
	public void runCommand(String[] parameters) {
		if (parameters.length <= 0) {
			ShowCommands(1);
		} else if (StringUtils.isNumeric(parameters[0])) {
			int page = Integer.parseInt(parameters[0]);
			ShowCommands(page);
		} else {
			Mod module = ModuleManager.INSTANCE.getModuleRegardOfCases(parameters[0]);
			if (module == null) {
				ChatUtil.insertMessage("Could not find Module '" + parameters[0] + "'.", ChatUtil.MessageType.WARNING);
			} else {
				String title = "------------ " + Formatting.AQUA + module.getName() + " Help" + Formatting.RESET + " ------------";
				String unformatted_title = "------------ " + module.getName() + " Help ------------";
				ChatUtil.insertMessage(title, ChatUtil.MessageType.NORMAL);
				ChatUtil.insertMessage("Name: " + Formatting.AQUA + module.getName() + Formatting.RESET, ChatUtil.MessageType.NORMAL);
				ChatUtil.insertMessage("Description: " + Formatting.AQUA + module.getDescription() + Formatting.RESET, ChatUtil.MessageType.NORMAL);
				ChatUtil.insertMessage("Keybind: " + Formatting.AQUA + module.getKeyName() + Formatting.RESET, ChatUtil.MessageType.NORMAL);
				ChatUtil.insertMessage("-".repeat(unformatted_title.length() - 2), ChatUtil.MessageType.NORMAL); // mc font characters are not the same width but eh..
			}
		}

	}

	private void ShowCommands(int page) {
		String title = "------------ Help [Page " + page + " of 5] ------------";  // TODO: remove hardcoded page length
		ChatUtil.insertMessage(title, ChatUtil.MessageType.NORMAL);
		ChatUtil.insertMessage("Use " + Formatting.AQUA + ".sc help [n]" + Formatting.RESET + " to get page n of help.", ChatUtil.MessageType.NORMAL);

		// Fetch the commands and dislays their syntax on the screen.
		HashMap<String, Command> commands = Client.getInstance().commandManager.getCommands();
		Set<String> keySet = commands.keySet();
		ArrayList<String> listOfCommands = new ArrayList<String>(keySet);
		 
		for (int i = (page - 1) * indexesPerPage; i <= (page * indexesPerPage); i++) {
			if (i >= 0 && i < Client.getInstance().commandManager.getNumOfCommands()) {
				ChatUtil.insertMessage(" .sc " + listOfCommands.get(i), ChatUtil.MessageType.NORMAL);
			}
		}
		ChatUtil.insertMessage("-".repeat(title.length() - 2), ChatUtil.MessageType.NORMAL); // mc font characters are not the same width but eh..
	}

    @Override
    public String[] getAutocorrect(List<String> parameters) {
		// TODO Auto-generated method stub
		CommandManager cm = Client.getInstance().commandManager;
		int numCmds = cm.getNumOfCommands();
		String[] commands = new String[numCmds];

		Set<String> cmds = Client.getInstance().commandManager.getCommands().keySet();
		int i = 0;
        for (String x : cmds)
        	commands[i++] = x;
		
		return commands;
	}

}
