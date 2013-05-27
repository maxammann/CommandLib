package com.p000ison.dev.commandlib.commands;

import com.p000ison.dev.commandlib.*;

import java.util.LinkedList;
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
        int size = executor.countCommands();

        final int page = information.getPage(size);
        final int start = information.getStartIndex(page, size);
        final int end = information.getEndIndex(page, size);

        final List<Command> commands = executor.getCommands();

        createLines(commands, sender, end, new HelpData(start), false);

    }

    private void createLines(List<Command> commands, CommandSender sender, int end, HelpData subReturn, boolean subCommands) {
        for (Command command : commands) {
            subReturn.global++;


            if (subCommands) {
                subReturn.addCommand(command);

                if (showHelp(sender, subReturn.lastCommands.getLast())) {
                    sender.sendMessage(createLine(subReturn.lastCommands));
                }
            } else {
                if (showHelp(sender, command)) {
                    sender.sendMessage(createLine(command));
                }
                subReturn.lastCommands.clear();
                subReturn.addCommand(command);
            }

            createLines(command.getSubCommands(), sender, end, subReturn, true);

            if (subReturn.global == end) {
                return;
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

    private static class HelpData {
        private int global;
        private LinkedList<Command> lastCommands = new LinkedList<Command>();

        private HelpData(int global) {
            this.global = global;

        }

        public void addCommand(Command cmd) {
            lastCommands.add(cmd);
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

    private String createLine(LinkedList<Command> commands) {

        StringBuilder identifiers = new StringBuilder();

        for (Command command : commands) {
            identifiers.append(command.getIdentifiers()[0]).append(' ');
        }

        Command lastCommand = commands.getLast();

        String usage = lastCommand.getUsage();


        StringBuilder argumentsString = new StringBuilder().append(' ');
        for (Argument argument : lastCommand.getArguments()) {
            argumentsString.append('<').append(argument.getName()).append('>').append(' ');
        }

        return String.format(format, identifiers.toString(), argumentsString.toString(), usage);
    }
}
