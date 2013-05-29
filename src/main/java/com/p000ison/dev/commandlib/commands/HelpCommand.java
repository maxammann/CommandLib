package com.p000ison.dev.commandlib.commands;

import com.p000ison.dev.commandlib.*;

import java.util.List;
import java.util.Map;

/**
 * Represents a HelpCommand
 */
public class HelpCommand extends Command {

    private final CommandExecutor executor;
    private final Map<Command, String> help;

    public HelpCommand(CommandExecutor executor, String name, String usage, String page, String identifiers) {
        super(name, usage);
        this.executor = executor;

        addArgument(new Argument(page, true, true));
        setIdentifiers(identifiers);

        final List<Command> commands = executor.getCommands();
        HelpCommandBuilder builder = new HelpCommandBuilder(commands);
        builder.buildHelp();
        help = builder.getOutput();
    }

    @Override
    public void execute(CommandSender sender, CallInformation information) {
        int size = executor.countCommands();

        final int page = information.getPage(size);
        final int start = information.getStartIndex(page, size);
        final int end = information.getEndIndex(page, size);

        sendHelp(sender, help, end - 1, start);
    }

    public void sendHelp(CommandSender sender, Map<Command, String> commands, int endIndex, int startIndex) {
        int current = startIndex;

        for (Map.Entry<Command, String> entry : commands.entrySet()) {
            if (current > endIndex) {
                return;
            }

            Command command = entry.getKey();
            String help = entry.getValue();

            if (current >= startIndex) {
                if (showHelp(sender, command)) {
                    current++;

                    sender.sendMessage(help);
                }
            } else {
                current++;
            }
        }
    }

    private boolean showHelp(CommandSender sender, Command command) {
        if (command == null) {
            return false;
        }
        if (command instanceof HelpEntryValidation && !((HelpEntryValidation) command).displayHelpEntry(sender)) {
            return false;
        } else if (command instanceof AnnotatedCommand && !((AnnotatedCommand) command).isExecutionAllowed(sender)) {
            return false;
        }

        return true;
    }
}
