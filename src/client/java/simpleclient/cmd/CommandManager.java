package simpleclient.cmd;

import java.lang.reflect.Field;
import java.util.HashMap;

import net.minecraft.util.Formatting;
import simpleclient.cmd.commands.*;
import simpleclient.utils.ChatUtil;

public class CommandManager {
	private HashMap<String, Command> commands = new HashMap<String, Command>();
	
	//Commands module
    public final CmdTest testCommand = new CmdTest();
	public final CmdAutoEat autoeat = new CmdAutoEat();
	public final CmdEntityESP entityesp = new CmdEntityESP();
    public final CmdBlockESP blockesp = new CmdBlockESP();
	public final CmdTP tp = new CmdTP();
	public final CmdHelp help = new CmdHelp();
	public final CmdFriends friends = new CmdFriends();

	public CommandManager() {
		try
		{
			for(Field field : CommandManager.class.getDeclaredFields())
			{
				if (!Command.class.isAssignableFrom(field.getType())) 
					continue;
				Command cmd = (Command)field.get(this);
				commands.put(cmd.getName(), cmd);
			}
		}catch(Exception e)
		{
			System.out.println("Error initializing SimpleClient commands.");
			System.out.println(e.getStackTrace().toString());
		}
	}
	
	/**
	 * Gets the command by a given syntax.
	 * 
	 * @param string The syntax (command) as a string.
	 * @return The Command Object associated with that syntax.
	 */
	public Command getCommandBySyntax(String string) {
		return this.commands.get(string);
	}

	/**
	 * Gets all of the Commands currently registered.
	 * 
	 * @return List of registered Command Objects.
	 */
	public HashMap<String, Command> getCommands() {
		return this.commands;
	}

	/**
	 * Gets the total number of Commands.
	 * @return The number of registered Commands.
	 */
	public int getNumOfCommands() {
		return this.commands.size();
	}

	/**
	 * Runs a command.
	 * @param commandIn A list of Command Parameters given by a "split" message.
	 */
	public void command(String[] commandIn) {
		try {
			
			// Get the command from the user's message. (Index 0 is Username)
			Command command = commands.get(commandIn[1]);

			// If the command does not exist, throw an error.
			if (command == null)
				ChatUtil.insertMessage("Command not found. Type " + Formatting.AQUA + ".sc help" + Formatting.YELLOW + " for a list of commands." + Formatting.RESET, ChatUtil.MessageType.WARNING);
			else {
				// Otherwise, create a new parameter list.
				String[] parameterList = new String[commandIn.length - 2];
				if (commandIn.length > 1) {
					for (int i = 2; i < commandIn.length; i++) {
						parameterList[i - 2] = commandIn[i];
					}
				}
				
				// Runs the command.
				command.runCommand(parameterList);
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			ChatUtil.insertMessage("Command not found. Type " + Formatting.AQUA + ".sc help" + Formatting.YELLOW + " for a list of commands." + Formatting.RESET, ChatUtil.MessageType.WARNING);
		} catch (InvalidSyntaxException e) {
			e.PrintToChat();
		}
	}
}
