package com.p000ison.dev.commandlib.commands;

import com.p000ison.dev.commandlib.Argument;
import com.p000ison.dev.commandlib.Command;

import java.util.*;

/**
 * Represents a HelpCommandBuilder
 */
class HelpCommandBuilder {
    private List<Command> commands;

    private Deque<Command> commandStack = new LinkedList<Command>();

    private Map<Command, String> output = new HashMap<Command, String>();

    HelpCommandBuilder(List<Command> commands) {
        this.commands = commands;
    }

    public void buildHelp() {
        buildHelp(commands);
    }

    public boolean buildHelp(List<Command> commands) {
        if (commands.isEmpty()) {
            return true;
        }

        for (Command command : commands) {
            commandStack.add(command);

            processCommands(commandStack);

            if (buildHelp(command.getSubCommands())) {
                commandStack.pollLast();
            }
        }

        //Normally finished
        if (!commandStack.isEmpty()) {
            commandStack.pollLast();
        }
        return false;
    }

    private void processCommands(Deque<Command> commands) {
        if (commands.isEmpty()) {
            return;
        }

        Command lastCommand = commands.getLast();
        output.put(lastCommand, createLine(commands));
    }

    protected String createLine(Deque<Command> commands) {
        if (commands.isEmpty()) {
            return null;
        }
        StringBuilder line = new StringBuilder();

        for (Command command : commands) {
            appendIdentifier(line, command);
        }

        Command lastCommand = commands.getLast();
        appendUsage(line, lastCommand);
        appendArguments(line, lastCommand);

        return line.toString().trim();
    }

    private void appendIdentifier(StringBuilder builder, Command command) {
        builder.append(command.getIdentifiers().length > 0 ? command.getIdentifiers()[0] : "null").append(' ');
    }

    private void appendUsage(StringBuilder builder, Command command) {
        builder.append(command.getUsage()).append(' ');
    }

    private void appendArguments(StringBuilder builder, Command command) {
        for (Argument argument : command.getArguments()) {
            builder.append('<').append(argument.getName()).append('>').append(' ');
        }
    }

    Map<Command, String> getOutput() {
        return output;
    }
}
