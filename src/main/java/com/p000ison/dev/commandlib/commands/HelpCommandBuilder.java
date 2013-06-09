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

    private Map<Command, String> output = new LinkedHashMap<Command, String>();


    /**
     * -ids-args-usage
     */
    private String format;

    HelpCommandBuilder(List<Command> commands, String format) {
        this.commands = commands;
        this.format = format;
    }

    public void buildHelp() {
        buildHelp(commands);
    }

    public void buildHelp(List<Command> commands) {
        for (Command command : commands) {
            commandStack.add(command);

            processCommands(commandStack);

            buildHelp(command.getSubCommands());
        }

        commandStack.pollLast();
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

        StringBuilder builder = new StringBuilder();
        String output = format;

        for (Command command : commands) {
            appendIdentifier(builder, command);
        }

        output = output.replace("-ids", builder.toString());
        builder.setLength(0);

        Command lastCommand = commands.getLast();

        appendArguments(builder, lastCommand);
        output = output.replace("-args", builder.toString());
        builder.setLength(0);

        appendUsage(builder, lastCommand);
        output = output.replace("-usage", builder.toString());

        return output;
    }

    private void appendIdentifier(StringBuilder builder, Command command) {
        if (command.getIdentifiers() == null) {
            throw new IllegalStateException("The identifiers of the command " + command.toString() + " are null!");
        }
        builder.append(command.getIdentifiers().length > 0 ? command.getIdentifiers()[0] : "null").append(' ');
    }

    private void appendUsage(StringBuilder builder, Command command) {
        builder.append(command.getUsage()).append(' ');
    }

    private void appendArguments(StringBuilder builder, Command command) {
        for (Argument argument : command.getArguments()) {
            builder.append(argument.isRequired() ? '<' : '[').append(argument.getName()).append(argument.isRequired() ? '>' : ']').append(' ');
        }
    }

    Map<Command, String> getOutput() {
        return output;
    }
}
