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

    public HelpCommand(CommandExecutor executor, String name, String usage, String page, String identifiers, String format) {
        super(name, usage);
        this.executor = executor;

        addArgument(new Argument(page, true, true));
        setIdentifiers(identifiers);

        final List<Command> commands = executor.getCommands();
        HelpCommandBuilder builder = new HelpCommandBuilder(commands, format);
        builder.buildHelp();
        help = builder.getOutput();
    }

    @Override
    public void execute(CommandSender sender, CallInformation information) {
        int size = countCommands(sender, executor.getCommands());

        final int page = information.getPage(size);
        final int start = information.getStartIndex(page, size);
        final int end = information.getEndIndex(page, size);

        sendHelp(sender, help, end - 1, start);
    }


    private int countCommands(CommandSender sender, List<Command> commands) {
        int count = 0;
        for (Command command : commands) {
            if (!allowExecution(sender)) {
                continue;
            }
            count++;
            count += countCommands(sender, command.getSubCommands());
        }

        return count;
    }

    public void sendHelp(CommandSender sender, Map<Command, String> commands, int endIndex, int startIndex) {
        int current = 0;

        for (Map.Entry<Command, String> entry : commands.entrySet()) {
            if (current > endIndex) {
                return;
            }

            Command command = entry.getKey();
            String help = entry.getValue();

            if (!command.allowExecution(sender)) {
                continue;
            }

            if (current >= startIndex) {
                sender.sendMessage(help);
            }

            current++;
        }
    }
}
