package com.p000ison.dev.commandlib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * This represents a command, which was created to execute a method which was annotated by {@link CommandHandler}
 */
public class AnnotatedCommand extends Command {
    private final Method executeMethod;
    private final Object methodInstance;
    private ExecutionRestriction executionRestriction;

    /**
     * Creates a new command based on information given by an annotation
     *
     * @param name           The name of the command
     * @param usage          The usage of the command
     * @param identifiers    The identifiers
     * @param arguments      The arguments
     * @param executeMethod  The method which gets executed
     * @param methodInstance The instance of the method or null
     */
    AnnotatedCommand(final String name, final String usage,
                     final String[] identifiers,
                     final List<Argument> arguments,
                     final Method executeMethod, final Object methodInstance) {

        super(name, usage);
        super.setIdentifiers(identifiers).addArguments(arguments);
        this.executeMethod = executeMethod;
        this.methodInstance = methodInstance;
    }


    @Override
    public final void execute(final CommandSender sender, final CallInformation information) {
        if (executeMethod != null) {
            try {
                executeMethod.invoke(methodInstance, sender, information);
            } catch (IllegalAccessException e) {
                throw new CommandException(this, e, "No access to the method: %s", executeMethod.getName());
            } catch (InvocationTargetException e) {
                throw new CommandException(this, e, "Exception in method: %s", executeMethod.getName());
            }
        }
    }

    @Override
    public final boolean allowExecution(CommandSender sender) {
        return !(executionRestriction != null && !executionRestriction.allowExecution(sender, this));
    }

    public AnnotatedCommand setExecutionRestriction(ExecutionRestriction executionRestriction) {
        this.executionRestriction = executionRestriction;
        return this;
    }


    public static interface ExecutionRestriction {

        boolean allowExecution(CommandSender sender, Command command);
    }
}
