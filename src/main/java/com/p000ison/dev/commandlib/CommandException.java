package com.p000ison.dev.commandlib;

/**
 * Represents a CommandException
 */
public class CommandException extends RuntimeException {

    private Command command;

    public CommandException(Command command, String message, Object... args) {
        this(command, null, message, args);
    }

    public CommandException(Command command, Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause);
        this.command = command;
    }

    public CommandException(Command command, Throwable cause) {
        super(cause);
        this.command = command;
    }

    public Command getFailedCommand() {
        return command;
    }
}
