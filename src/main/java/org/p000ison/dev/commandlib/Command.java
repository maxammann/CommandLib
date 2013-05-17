package org.p000ison.dev.commandlib;

import java.util.*;

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


    /**
     * Setups this command based on given information
     *
     * @param name        The name of the command
     * @param usage       The usage of the command
     * @param identifiers The identifiers of the command
     */
    void setup(final String name, final String usage, final String[] identifiers) {
        this.identifiers = identifiers;
        this.usage = usage;
        this.name = name;
    }

    public Command(final String name, final String usage) {
        this.name = name;
        this.usage = usage;
    }

    public Command(final String name) {
        this(name, null);
    }

    Command(final String name, final String usage, final String[] identifiers, final List<Argument> arguments) {
        this.arguments.addAll(arguments);
        setup(name, usage, identifiers);
    }

    public Command() {
    }

    public final String getName() {
        return name;
    }

    public final String getUsage() {
        return usage;
    }

    public final String[] getIdentifiers() {
        return identifiers;
    }

    public final void setIdentifiers(String... identifiers) {
        this.identifiers = identifiers;
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

    public Command addArguments(Argument argument) {
        this.arguments.add(argument);
        return this;
    }


    //================================================================================
    //  Execute methods
    //================================================================================


    public void execute(final CommandSender sender, final CallInformation information) {
        System.out.println("Command executed!\n" + this.toString() + "\n" + information);
    }

    void executeCallMethods(CommandSender sender, CallInformation info) {
        for (Command command : getCallMethods()) {
            command.execute(sender, info);
        }
    }

    //================================================================================
    // Modify methods
    //================================================================================

    final void finish() {
        subCommands = Collections.unmodifiableList(subCommands);
        callMethods = Collections.unmodifiableList(callMethods);
        permissions = Collections.unmodifiableList(permissions);
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final void setUsage(String usage) {
        this.usage = usage;
    }

    public final void addArgument(Argument argument) {
        this.arguments.add(argument);
    }

    void addArguments(Collection<Argument> arguments) {
        this.arguments.addAll(arguments);
    }

    public final void addCallMethod(Command command) {
        this.callMethods.add(command);
    }

    public final void addSubCommand(Command subCommand) {
        this.subCommands.add(subCommand);
    }

    void addSubCommands(Collection<Command> subCommands) {
        this.subCommands.addAll(subCommands);
    }

    public final void addPermission(String permission) {
        this.permissions.add(permission);
    }

    final void addPermissions(Collection<String> permissions) {
        this.permissions.addAll(permissions);
    }


    public final void addAlias(Command alias) {
        this.callMethods.add(alias);
    }

    public final void addAliases(Collection<Command> aliases) {
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
}
