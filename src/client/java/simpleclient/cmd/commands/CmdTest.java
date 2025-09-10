package simpleclient.cmd.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simpleclient.Client;
import simpleclient.cmd.Command;
import simpleclient.cmd.InvalidSyntaxException;
import simpleclient.utils.ChatUtil;

import java.util.List;

public class CmdTest extends Command {

    public CmdTest() {
        super("testCommand", "Test command", "[set] [value]");
    }

    @Override
    public void runCommand(String[] parameters) throws InvalidSyntaxException {
        if (parameters.length != 2)
            throw new InvalidSyntaxException(this);

        switch (parameters[0]) {
            case "setTestValue":
                String value = parameters[1].toLowerCase();
                if (value.isEmpty()) {
                    ChatUtil.insertMessage("Please enter a string.", ChatUtil.MessageType.WARNING);
                } else {
                    Client.INSTANCE.testValue = value;
                    ChatUtil.insertMessage("Test value set to [" + value + "].", ChatUtil.MessageType.NORMAL);
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
            case "setTestValue":
                return new String[] {"STRINGVALUE"};
            default:
                return new String[] {"setTestValue"};
        }
    }
}
