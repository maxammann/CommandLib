package com.p000ison.dev.commandlib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Represents a AnnotatedCommand
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
        if (!isExecutionAllowed(sender)) {
            return;
        }

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

    public boolean isExecutionAllowed(CommandSender sender) {
        return !(executionRestriction != null && !executionRestriction.allowExecution(sender, this));
    }

    public void setExecutionRestriction(ExecutionRestriction executionRestriction) {
        this.executionRestriction = executionRestriction;
    }
}
