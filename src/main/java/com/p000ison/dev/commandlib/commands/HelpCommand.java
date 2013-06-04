package com.p000ison.dev.commandlib.commands;

import com.p000ison.dev.commandlib.*;

import java.util.List;
import java.util.Map;

/**
 * Represents a HelpCommand
 */
public class HelpCommand extends Command {

    private final CommandExecutor executor;
    private Map<Command, String> help;

    public HelpCommand(CommandExecutor executor, String name, String usage, String page, String identifiers, String format) {
        super(name, usage);
        this.executor = executor;
        addArgument(new Argument(page, true, true));
        setIdentifiers(identifiers);
    }

    public void buildHelpCache(String format) {
        HelpCommandBuilder builder = new HelpCommandBuilder(executor.getCommands(), format);
        builder.buildHelp();
        help = builder.getOutput();
    }

    public String getHelp(Command command) {
        if (help == null) {
            throw new IllegalStateException("Help command cache is empty!");
        }

        return help.get(command);
    }

    @Override
    public void execute(CommandSender sender, CallInformation information) {
        if (help == null) {
            throw new IllegalStateException("Help command cache is empty!");
        }
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

    private void sendHelp(CommandSender sender, Map<Command, String> commands, int endIndex, int startIndex) {
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
