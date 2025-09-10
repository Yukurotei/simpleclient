package simpleclient.cmd.commands;

import simpleclient.Client;
import simpleclient.cmd.Command;
import simpleclient.cmd.InvalidSyntaxException;
import simpleclient.utils.ChatUtil;

import java.util.List;

public class CmdFriends extends Command{

	public CmdFriends() {
		super("friend", "add or remove a friend", "[add/remove/show] [value]");
	}
	
	@Override
	public void runCommand(String[] parameters) throws InvalidSyntaxException {
		if (parameters.length != 2 && !parameters[0].equals("show") ) {
			System.out.println(parameters[0]);
			throw new InvalidSyntaxException(this);
		}
		
		switch (parameters[0]) {
		case "add":
			String addName = parameters[1].toLowerCase();
			if (Client.INSTANCE.doesFriendExist(addName)) {
				ChatUtil.insertMessage("Existing friend called (" + addName + ") found", ChatUtil.MessageType.WARNING);
				return;
			}
			Client.INSTANCE.addFriend(addName);
			ChatUtil.insertMessage("Successfully added (" + addName + ") as a friend", ChatUtil.MessageType.NORMAL);
			break;
		case "remove":
			String removeName = parameters[1].toLowerCase();
			boolean removed = Client.INSTANCE.removeFriend(removeName);
			if (removed) {
				ChatUtil.insertMessage("Successfully removed (" + removeName + ") from the friend list", ChatUtil.MessageType.NORMAL);
			} else {
				ChatUtil.insertMessage("Can not find (" + removeName + ") in the friend list", ChatUtil.MessageType.WARNING);
			}
			break;
		case "show":
				if (Client.INSTANCE.getFriends().size() < 1) {
					ChatUtil.insertMessage("No friends in the friend list", ChatUtil.MessageType.WARNING);
					return;
				}
				ChatUtil.insertMessage("Friends:", ChatUtil.MessageType.NORMAL);
				for (int i = 0; i < Client.INSTANCE.getFriends().size(); i++) {
					ChatUtil.insertMessage("" + (i + 1) + ", " + Client.INSTANCE.getFriends().get(i), ChatUtil.MessageType.NORMAL);
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
		case "add":
			return new String[] { "USERNAME" };
		case "remove":
			return new String[] { "USERNAME" };
		case "show":
			return new String[] { };
		default:
			return new String[] { "add", "remove", "show" };
		}
	}

}
