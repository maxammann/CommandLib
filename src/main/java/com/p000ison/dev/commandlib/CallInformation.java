package com.p000ison.dev.commandlib;

import java.util.Arrays;

/**
 * Represents a CallInformation
 */
public class CallInformation {

    private final String identifier;
    private final String[] arguments;
    private final Command command;
    private final CommandSender sender;

    public CallInformation(String identifier, String[] arguments, Command command, CommandSender sender) {
        this.identifier = identifier;
        this.arguments = arguments;
        this.command = command;
        this.sender = sender;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String[] getArguments() {
        return arguments;
    }

    public CommandSender getSender() {
        return sender;
    }

    public void reply(String message) {
        getSender().sendMessage(message);
    }

    public int getPage() {
        for (final Argument argument : command.getArguments()) {
            if (argument.isPage()) {
                int integer = getInteger(argument.getPosition());

                if (integer != -1) {
                    return integer;
                }

                break;
            }
        }
        return 0;
    }

    public int getInteger(int index) {
        try {
            return CommandExecutor.parseInt(getArguments()[index]);
        } catch (NumberFormatException e) {
            return -1;
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "CallInformation{" +
                "identifier='" + identifier + '\'' +
                ", arguments=" + Arrays.toString(arguments) +
                ", sender=" + sender +
                '}';
    }
}
