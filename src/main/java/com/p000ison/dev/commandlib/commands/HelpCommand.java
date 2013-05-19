package com.p000ison.dev.commandlib.commands;

import com.p000ison.dev.commandlib.*;

import java.util.List;

/**
 * Represents a HelpCommand
 */
public class HelpCommand extends Command {

    private final CommandExecutor executor;
    private final String format;
    private final int maxCommandsPerPage;

    public HelpCommand(CommandExecutor executor, String name, String usage, String identifier, String format, int maxCommands) {
        super(name, usage);
        this.executor = executor;
        this.format = format;
        this.maxCommandsPerPage = maxCommands;

        addArgument(new Argument("page", 0, true, false, true, true));
        setIdentifiers(identifier, "help");
    }

    @Override
    public void execute(CommandSender sender, CallInformation information) {
        final int page = information.getPage();

        int start = page * maxCommandsPerPage;
        int end = (page + 1) * maxCommandsPerPage;

        final List<Command> commands = executor.getCommands();
        final int length = commands.size();

        //check if start and end are in bounds
        if (start > length - maxCommandsPerPage) {
            start = length - maxCommandsPerPage - 1;
        }

        if (end > length) {
            end = length - 1;
        }

        for (int i = start; i <= end; i++) {
            Command command = commands.get(i);
            sender.sendMessage(createLine(command));
        }
    }

    private String createLine(Command command) {

        String identifier = command.getIdentifiers()[0];
        String usage = command.getUsage();

        StringBuilder argumentsString = new StringBuilder().append(' ');
        for (Argument argument : command.getArguments()) {
            argumentsString.append('<').append(argument.getName()).append('>').append(' ');
        }

        return String.format(format, identifier, argumentsString.toString(), usage);
    }
}
