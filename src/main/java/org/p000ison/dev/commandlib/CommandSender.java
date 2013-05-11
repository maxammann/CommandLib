package org.p000ison.dev.commandlib;

/**
 * Represents a CommandSender
 */
public interface CommandSender {

    void sendMessage(String message);

    void sendMessage(String message, final Object... args);

    boolean hasPermission(final Command permission);
}
