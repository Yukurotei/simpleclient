package simpleclient.cmd.commands;

import net.minecraft.registry.Registries;
import simpleclient.cmd.Command;
import simpleclient.cmd.InvalidSyntaxException;
import simpleclient.module.ModuleManager;
import simpleclient.module.render.BlockESP;
import simpleclient.ui.Color;
import simpleclient.utils.BasicUtil;
import simpleclient.utils.ChatUtil;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class CmdBlockESP extends Command {

    // This will be a one-time lookup for efficiency
    private static final String[] ALL_BLOCK_NAMES;
    private static final Set<String> ALL_BLOCK_NAMES_SET;

    // Static initializer to load all block names on startup
    static {
        Stream<String> blockNames = Registries.BLOCK.stream()
                .map(block -> Registries.BLOCK.getId(block).getPath());

        ALL_BLOCK_NAMES = blockNames.toArray(String[]::new);
        ALL_BLOCK_NAMES_SET = new HashSet<>(Arrays.asList(ALL_BLOCK_NAMES));
    }


    public CmdBlockESP() {
        super("blockesp", "Configure blockesp", "[addEntry/removeEntry] [value] [R(0-255)] [G(0-255)] [B(0-255)]");
    }

    @Override
    public void runCommand(String[] parameters) throws InvalidSyntaxException {
        if (parameters.length != 5 && Objects.equals(parameters[0], "addEntry")) {
            throw new InvalidSyntaxException(this);
        } else if (parameters.length != 2 && Objects.equals(parameters[0], "removeEntry")) {
            throw new InvalidSyntaxException(this);
        }

        switch (parameters[0]) {
            case "addEntry":
                //Parameter type checks
                if (!BasicUtil.isNumeric(parameters[2])) //R
                    throw new InvalidSyntaxException(this);
                if (!BasicUtil.isNumeric(parameters[3])) //G
                    throw new InvalidSyntaxException(this);
                if (!BasicUtil.isNumeric(parameters[4])) //B
                    throw new InvalidSyntaxException(this);
                //Valid number check
                if (Integer.parseInt(parameters[2]) > 255) {
                    throw new InvalidSyntaxException(this);
                }
                if (Integer.parseInt(parameters[3]) > 255) {
                    throw new InvalidSyntaxException(this);
                }
                if (Integer.parseInt(parameters[4]) > 255) {
                    throw new InvalidSyntaxException(this);
                }
                //Now we register block
                ((BlockESP)ModuleManager.INSTANCE.getModule("BlockESP")).blockEntries.put(parameters[1].toLowerCase(), new Color(Integer.parseInt(parameters[2]),
                        Integer.parseInt(parameters[3]), Integer.parseInt(parameters[4])));
                ChatUtil.insertMessage("Added [" + parameters[1].toLowerCase() + "] to block entries.", ChatUtil.MessageType.NORMAL);
                mc.worldRenderer.reload();
                break;
            case "removeEntry":
                Color removedColor = ((BlockESP)ModuleManager.INSTANCE.getModule("BlockESP")).blockEntries.remove(parameters[1].toLowerCase());
                if (removedColor == null) {
                    ChatUtil.insertMessage("Block is not in entry list", ChatUtil.MessageType.WARNING);
                    break;
                }
                ChatUtil.insertMessage("Removed block from entry list.", ChatUtil.MessageType.NORMAL);
                break;
            case "getEntries":
                Map<String, Color> blockEntry = ((BlockESP)ModuleManager.INSTANCE.getModule("BlockESP")).blockEntries;
                if (blockEntry.isEmpty()) {
                    ChatUtil.insertMessage("No entries exists.", ChatUtil.MessageType.WARNING);
                    break;
                }
                ChatUtil.insertMessage("Block entries:", ChatUtil.MessageType.NORMAL);
                for (Map.Entry<String, Color> entry : blockEntry.entrySet()) {
                    ChatUtil.insertMessage(entry.getKey() + ", RGB(" + entry.getValue().r + "," + entry.getValue().g + "," + entry.getValue().b + ")", ChatUtil.MessageType.NORMAL);
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
            case "addEntry":
            case "removeEntry":
                return ALL_BLOCK_NAMES;
            case "R":
                return new String[] {"G"};
            case "G":
                return new String[] {"B"};
            default:
                if (ALL_BLOCK_NAMES_SET.contains(previousParameter)) {
                    return new String[] {"R"};
                }
                if (previousParameter.equals("blockesp")) {
                    return new String[] {"addEntry", "removeEntry", "getEntries"};
                }
                //return new String[] {previousParameter};
                //.sc blockesp addEntry ENTRY 0 0 0
                int spaceAmount = parameters.size() - 1;
                if (spaceAmount == 2) {
                    return new String[] {"addEntry", "removeEntry", "getEntries"};
                } else if (spaceAmount == 3) {
                    return ALL_BLOCK_NAMES;
                } else if (spaceAmount == 4) {
                    return new String[] {"R"};
                } else if (spaceAmount == 5) {
                    return new String[] {"G"};
                } else if (spaceAmount == 6) {
                    return new String[] {"B"};
                } else {
                    return new String[0];
                }
        }
    }
}
