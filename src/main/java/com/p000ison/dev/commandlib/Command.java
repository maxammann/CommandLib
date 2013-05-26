package com.p000ison.dev.commandlib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Represents a Command
 */
public class Command {

    /**
     * The usage and the name of this command
     */
    private String usage, name;

    /**
     * A list of sub-commands, you can change this during runtime
     */

    private List<Command> subCommands = new ArrayList<Command>();
    /**
     * A array of the identifiers to detect this command, you can NOT change this during runtime
     */
    private String[] identifiers;
    /**
     * A List of the arguments for this command, you can change this during runtime
     */

    private List<Argument> arguments = new ArrayList<Argument>();

    /**
     * A List of the callMethods for this command, you can change this during runtime
     * If this command is being executed, those methods get called, too (Aliases)
     */

    private List<Command> callMethods = new ArrayList<Command>();

    /**
     * A List of the permission to execute this command, you can change this during runtime
     */

    private List<String> permissions = new ArrayList<String>();


    private boolean needAllPermissions;
    private boolean infinite;

    protected Command(final String name, final String usage) {
        this.name = name;
        this.usage = usage;
    }

    protected Command(final String name) {
        this(name, null);
    }

    /**
     * Setups this command based on given information
     *
     * @param name        The name of the command
     * @param usage       The usage of the command
     * @param identifiers The identifiers of the command
     */
    private void setup(final String name, final String usage, final String[] identifiers) {
        this.identifiers = identifiers;
        this.usage = usage;
        this.name = name;
    }

    public Command() {
    }

    //================================================================================
    //  Public getter methods
    //================================================================================

    public final String getName() {
        return name;
    }

    public final String getUsage() {
        return usage;
    }

    public final String[] getIdentifiers() {
        return identifiers;
    }

    public final boolean isIdentifier(String identifier) {
        for (final String string : identifiers) {
            if (identifier.equals(string)) {
                return true;
            }
        }

        return false;
    }


    public final List<Command> getSubCommands() {
        return subCommands;
    }

    public final int getMaxArguments() {
        return arguments.size();
    }

    public final int getMinArguments() {
        int min = 0;

        for (final Argument argument : arguments) {
            if (argument.isRequired()) {
                min++;
            }
        }

        return min;
    }


    public final List<Argument> getArguments() {
        return arguments;
    }


    public final List<Command> getCallMethods() {
        return callMethods;
    }


    public final List<String> getPermissions() {
        return permissions;
    }

    public final boolean hasCallMethods() {
        return !callMethods.isEmpty();
    }

    public final boolean hasPermission(CommandSender sender) {
        if (permissions.isEmpty()) {
            return true;
        }

        for (String perm : permissions) {
            if (sender.hasPermission(perm)) {
                if (!needAllPermissions) {
                    return true;
                }
            } else {
                if (needAllPermissions) {
                    return false;
                }
            }
        }


        return !needAllPermissions;
    }


    //================================================================================
    //  Public modify methods
    //================================================================================


    public final Command addArgument(Argument argument) {
        this.arguments.add(argument);
        return this;
    }

    public final Command addArgument(String name) {
        this.arguments.add(new Argument(name));
        return this;
    }

    public final Command addArgument(String name, boolean optional) {
        this.arguments.add(new Argument(name, optional));
        return this;
    }

    public final Command addArgument(String name, boolean optional, boolean page) {
        this.arguments.add(new Argument(name, optional, page));
        return this;
    }

    public final Command createArguments(int minArguments, int maxArguments, String[] names) {
        this.arguments = CommandExecutor.createArguments(maxArguments, minArguments, names);
        return this;
    }


    public final Command addPermission(String permission) {
        this.permissions.add(permission);
        return this;
    }


    public final Command setIdentifiers(String... identifiers) {
        this.identifiers = identifiers;
        return this;
    }


    public final Command addAlias(Command alias) {
        this.callMethods.add(alias);
        return this;
    }


    public final Command setName(String name) {
        this.name = name;
        return this;
    }


    public final Command setUsage(String usage) {
        this.usage = usage;
        return this;
    }


    public final Command setNeedAllPermissions(boolean needAllPermissions) {
        this.needAllPermissions = needAllPermissions;
        return this;
    }


    public final Command addSubCommand(Command subCommand) {
        this.subCommands.add(subCommand);
        return this;
    }

    //================================================================================
    //  Execute methods
    //================================================================================

    public void execute(final CommandSender sender, final CallInformation information) {
    }

    void executeCallMethods(CommandSender sender, CallInformation info) {
        for (Command command : getCallMethods()) {
            command.execute(sender, info);
        }
    }

    //================================================================================
    // Internal modify methods
    //================================================================================

    void check() {
        if (name == null) {
            throw new CommandException(this, "The command %s has no name!", this.getName());
        } else if (usage == null) {
            throw new CommandException(this, "The command %s has no usage!", this.getName());
        } else if (identifiers == null || identifiers.length == 0) {
            throw new CommandException(this, "The command %s has no identifiers!", this.getName());
        }
    }

    void addArguments(Collection<Argument> arguments) {
        this.arguments.addAll(arguments);
    }

    void addCallMethod(Command command) {
        this.callMethods.add(command);
    }

    void addSubCommands(Collection<Command> subCommands) {
        this.subCommands.addAll(subCommands);
    }

    void addPermissions(Collection<String> permissions) {
        this.permissions.addAll(permissions);
    }

    void addAliases(Collection<Command> aliases) {
        for (Command command : aliases) {
            command.addCallMethod(this);
        }
    }

    //================================================================================
    // Additional methods
    //================================================================================


    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", identifiers=" + Arrays.toString(identifiers) +
                ", maxArguments=" + getMaxArguments() +
                ", minArguments=" + getMinArguments() +
                '}';
    }

    @Override
    public boolean equals(Object command) {
        if (this == command) return true;
        if (!(command == null || getClass() != command.getClass()))
            if (name.equals(((Command) command).name)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public boolean isInfinite() {
        return infinite;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }
}
