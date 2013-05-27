package com.p000ison.dev.commandlib.commands;

import com.p000ison.dev.commandlib.*;

import java.util.List;

/**
 * Represents a HelpCommand
 */
public class HelpCommand extends Command {

    private final CommandExecutor executor;
    private final String format;

    public HelpCommand(CommandExecutor executor, String name, String usage, String page, String format, String identifiers) {
        super(name, usage);
        this.executor = executor;
        this.format = format;

        addArgument(new Argument(page, true, true));
        setIdentifiers(identifiers);
    }

    @Override
    public void execute(CommandSender sender, CallInformation information) {
        int size = executor.getCommands().size();

        for  (Command command : executor.getCommands()) {
            size += command.getSubCommands().size();
        }

        final int page = information.getPage(size);
        final int start = information.getStartIndex(page, size);
        final int end = information.getEndIndex(page, size);

        final List<Command> commands = executor.getCommands();

        all: for (int i = start; i < end; i++) {
            Command command = commands.get(i);

            if (command instanceof HelpEntryValidation && !((HelpEntryValidation) command).displayHelpEntry(sender)) {
                continue;
            } else if (command instanceof AnnotatedCommand && !((AnnotatedCommand) command).isExecutionAllowed(sender)) {
                continue;
            }

            sender.sendMessage(createLine(command));

            for (int j = 0; j < command.getSubCommands().size(); j++) {
                i++;

                if (i >= end) {
                    break all;
                }

                Command sub = command.getSubCommands().get(j);


                if (sub instanceof HelpEntryValidation && !((HelpEntryValidation) sub).displayHelpEntry(sender)) {
                    continue;
                } else if (sub instanceof AnnotatedCommand && !((AnnotatedCommand) sub).isExecutionAllowed(sender)) {
                    continue;
                }

                sender.sendMessage(createLine(sub));
            }
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
