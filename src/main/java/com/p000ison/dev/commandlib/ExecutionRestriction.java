package com.p000ison.dev.commandlib;

/**
 * Represents a ExecutionRestriction
 */
public interface ExecutionRestriction {

    boolean allowExecution(CommandSender sender, Command command);
}
