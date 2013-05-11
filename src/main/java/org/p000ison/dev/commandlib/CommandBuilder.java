package org.p000ison.dev.commandlib;

import java.util.*;

/**
 * Represents a CommandBuilder
 */
public class CommandBuilder {

    private final CommandExecutor executor;
    private String name;
    private String usage;
    private final Set<String> identifiers = new HashSet<String>();
    private final Set<Command> subCommands = new HashSet<Command>();
    private Set<Command> aliases = new HashSet<Command>();
    private List<Argument> arguments;
    private final Set<String> permissions = new HashSet<String>();
    private boolean subCommand;

    CommandBuilder(CommandExecutor executor) {
        this.executor = executor;
    }

    public CommandBuilder setName(String name) {

        this.name = name;
        return this;
    }

    public CommandBuilder setUsage(String usage) {
        this.usage = usage;
        return this;
    }

    public CommandBuilder addIdentifier(String identifier) {
        this.identifiers.add(identifier);
        return this;
    }

    public CommandBuilder addSubCommand(Command command) {
        subCommands.add(command);
        return this;
    }

    public CommandBuilder addAlias(Command command) {
        aliases.add(command);
        return this;
    }

    public CommandBuilder addArgument(Argument argument) {
        if (arguments == null) {
            arguments = new LinkedList<Argument>();
        }
        arguments.add(argument);
        return this;
    }

    public CommandBuilder createArguments(int minArguments, int maxArguments, String[] names) {
        this.arguments = AnnotatedCommand.createArguments(maxArguments, minArguments, names);
        return this;
    }

    public CommandBuilder addPermission(String permission) {
        permissions.add(permission);
        return this;
    }

    public void setSubCommand(boolean subCommand) {
        this.subCommand = subCommand;
    }

    public Command finish(Command command) {
        if (name == null || usage == null || identifiers.size() == 0) {
            throw new IllegalArgumentException("You need to set at least a name, a usage and an identifier!");
        }

        command.setup(name, usage, identifiers.toArray(new String[identifiers.size()]), subCommand);

        command.addAliases(aliases);
        command.addArguments(arguments == null ? new ArrayList<Argument>() : arguments);
        command.addSubCommands(subCommands);
        command.addPermissions(permissions);

        command.finish();

        return command;
    }
}
