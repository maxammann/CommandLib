package org.p000ison.dev.commandapi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a AnnotatedCommand
 */
public class AnnotatedCommand extends Command {
    private final Method executeMethod;
    private final Object methodInstance;

    /**
     * Creates a new command based on information given by an annotation
     *
     * @param name
     * @param usage
     * @param identifiers
     * @param maxArguments
     * @param minArguments
     * @param names
     * @param executeMethod
     * @param methodInstance
     */
    AnnotatedCommand(final String name, final String usage,
                     final String[] identifiers,
                     final int maxArguments, final int minArguments, final String[] names,
                     final boolean subCommand,
                     final Method executeMethod, final Object methodInstance) {

        super(name, usage, identifiers, createArguments(maxArguments, minArguments, names), subCommand);
        this.executeMethod = executeMethod;
        this.methodInstance = methodInstance;
    }

    static List<Argument> createArguments(final int maxArguments, final int minArguments, final String[] names) {
        final List<Argument> arguments = new ArrayList<Argument>();

        for (int i = 0; i < maxArguments; i++) {
            arguments.add(new Argument(names[i], i, i >= minArguments, false, false));
        }

        return arguments;
    }


    @Override
    public void execute(final CommandSender sender, final CallInformation information) {
        if (executeMethod != null) {
            try {
                executeMethod.invoke(methodInstance, sender, information);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
