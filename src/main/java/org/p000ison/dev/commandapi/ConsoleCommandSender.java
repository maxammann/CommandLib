package org.p000ison.dev.commandapi;

/**
 * Represents a ConsoleCommandSender
 */
public class ConsoleCommandSender implements CommandSender {
    @Override
    public void sendMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void sendMessage(String message, Object... args) {
        System.out.printf(message, args);
    }

    @Override
    public boolean hasPermission(Command permission) {
        return true;
    }
}
