package com.p000ison.dev.commandlib;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A CommandExecutor handles the registration of commands and executes commands.
 */
public abstract class CommandExecutor {

    private final List<Command> commands = new ArrayList<Command>(1);

    private int defaultElementsPerPage = 10;
    private boolean executeOnlySub = true;

    public void executeAll(CommandSender sender, String command) {

        String[] arguments = command.split(" ");
        if (arguments.length == 0) {
            return;
        }

        String identifier = arguments[0];

        if (arguments.length >= 1) {
            arguments = removeUntil(arguments, 1);
        }

        executeAll(sender, identifier, arguments);
    }

    public void executeAll(CommandSender sender, String identifier, String[] arguments) {
        if (executeAll(sender, identifier, arguments, commands) == CallResult.NOT_FOUND) {
            onCommandNotFound(sender);
        }
    }

    private CallResult executeAll(CommandSender sender, String identifier, String[] arguments, List<Command> currentCommands) {
        int argumentsNr = arguments.length;

        CallResult result = CallResult.NOT_FOUND;

        List<Command> helpCommands = new LinkedList<Command>(), permCommands = new LinkedList<Command>();

        for (Command command : currentCommands) {
            if (command.isIdentifier(identifier)) {

                CallResult subResult = null;
                if (argumentsNr > 0) {
                    subResult = executeAll(sender, arguments[0], removeUntil(arguments, 1), command.getSubCommands());
                }

                if (subResult == CallResult.SUCCESS && executeOnlySub) {
                    return CallResult.SUCCESS;
                }

                if (command.isInfinite() || argumentsNr < command.getMinArguments() || argumentsNr > command.getMaxArguments()) {
                    //TODO check if argument type is ok
                    if (subResult != CallResult.SUCCESS) {
                        helpCommands.add(command);
                        result = CallResult.DISPLAYED_COMMAND_HELP;
                        continue;
                    }
                }

                if (!command.hasPermission(sender)) {
                    permCommands.add(command);
                    result = CallResult.NO_PERMISSION;
                    continue;
                }

                CallInformation info = createCallInformation(command, sender, identifier, arguments);
                if (command.allowExecution(sender)) {
                    result = executeCommand(info);
                } else {
                    onExecutionBlocked(sender, command);
                    result = CallResult.BLOCKED;
                }
            }
        }

        if (result != CallResult.SUCCESS) {
            for (Command cmd : helpCommands) {
                onDisplayCommandHelp(sender, cmd);
            }

            for (Command cmd : permCommands) {
                onPermissionFailed(sender, cmd);
            }

            if (!permCommands.isEmpty()) {
                onPermissionFailed(sender);
            }
        }

        return result;
    }

    protected void executeCallMethods(CallInformation info) {
        for (Command command : info.getCommand().getCallMethods()) {
            onPreCommand(info);
            command.execute(info.getSender(), info);
            onPostCommand(info);
        }
    }

    protected CallResult executeCommand(CallInformation info) {
        Command command = info.getCommand();
        CommandSender sender = info.getSender();

        onPreCommand(info);
        command.execute(sender, info);
        onPostCommand(info);
        CallResult result = CallResult.SUCCESS;
        executeCallMethods(info);

        return result;
    }

    public void setExecuteOnlySubCommands(boolean executeOnlySub) {
        this.executeOnlySub = executeOnlySub;
    }

    static enum CallResult {
        SUCCESS,
        NOT_FOUND,
        DISPLAYED_COMMAND_HELP,
        NO_PERMISSION,
        BLOCKED
    }


    protected CallInformation createCallInformation(Command command, CommandSender sender, String identifier, String... arguments) {
        return new CallInformation(this, command, sender, identifier, arguments);
    }


    //================================================================================
    // Listening methods
    //================================================================================


    public abstract void onPreCommand(CallInformation info);

    public abstract void onExecutionBlocked(CommandSender sender, Command command);

    public abstract void onPostCommand(CallInformation info);

    public abstract void onDisplayCommandHelp(CommandSender sender, Command command);

    public abstract void onCommandNotFound(CommandSender sender);

    public abstract void onPermissionFailed(CommandSender sender, Command command);

    public abstract void onPermissionFailed(CommandSender sender);


    //================================================================================
    // Command registration
    //================================================================================


    public Command build() {
        return new Command();
    }

    public Command build(String name) {
        return new Command().setName(name);
    }

    public AnnotatedCommand build(Object instance, String commandName) {
        return findCommand(instance, commandName, null, instance.getClass().getDeclaredMethods());
    }

    public AnnotatedCommand build(Class clazz, String commandName) {
        return findCommand(null, commandName, null, clazz.getDeclaredMethods());
    }

    public AnnotatedCommand buildByMethod(Object instance, String methodName) {
        return findCommand(instance, null, methodName, instance.getClass().getDeclaredMethods());
    }

    public AnnotatedCommand buildByMethod(Class clazz, String methodName) {
        return findCommand(null, null, methodName, clazz.getDeclaredMethods());
    }

    public Command register(Command command) {
        if (!commands.contains(command)) {
            command.check();
            commands.add(command);
        }

        return command;
    }

    public AnnotatedCommand register(Object instance) {
        return register(instance, null, null, instance.getClass().getDeclaredMethods());
    }

    public AnnotatedCommand register(Class clazz) {
        return register(null, null, null, clazz.getDeclaredMethods());
    }

    public AnnotatedCommand register(Object instance, String commandName) {
        return register(instance, commandName, null, instance.getClass().getDeclaredMethods());
    }

    public AnnotatedCommand register(Class clazz, String commandName) {
        return register(null, commandName, null, clazz.getDeclaredMethods());
    }

    public AnnotatedCommand registerByMethod(Object instance, String methodName) {
        return register(instance, null, methodName, instance.getClass().getDeclaredMethods());
    }

    public AnnotatedCommand registerByMethod(Class clazz, String methodName) {
        return register(null, null, methodName, clazz.getDeclaredMethods());
    }

    private AnnotatedCommand register(Object instance, String commandName, String methodName, Method... methods) {
        AnnotatedCommand cmd = findCommand(instance, commandName, methodName, methods);
        if (cmd == null) {
            throw new CommandException(cmd, "Command not found in the class %s!", instance.getClass().getName());
        }

        register(cmd);

        return cmd;
    }

    private AnnotatedCommand findCommand(Object instance, String commandName, String methodName, Method... methods) {
        for (Method method : methods) {
            if (methodName != null && !method.getName().equals(methodName)) {
                continue;
            }

            CommandHandler annotation = getAnnotation(method);

            if (annotation != null) {
                if (commandName != null && !annotation.name().equals(commandName)) {
                    continue;
                }

                return createCommand(method, instance, annotation);
            }
        }

        return null;
    }

    public final boolean isRegistered(Command command) {
        return commands.contains(command);
    }

    public final List<Command> getCommands() {
        return commands;
    }


    public void setDefaultElementsPerPage(int defaultElementsPerPage) {
        this.defaultElementsPerPage = defaultElementsPerPage;
    }

    public int getDefaultElementsPerPage() {
        return defaultElementsPerPage;
    }

    //================================================================================
    // Internal helper methods
    //================================================================================


    private CommandHandler getAnnotation(Method method) {

        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 2 || parameterTypes[0] != CommandSender.class || parameterTypes[1] != CallInformation.class) {
            return null;
        }

        return method.getAnnotation(CommandHandler.class);
    }


    private AnnotatedCommand createCommand(Method method, Object instance, CommandHandler annotation) {
        return new AnnotatedCommand(annotation.name(), annotation.usage(),
                annotation.identifiers(),
                createArguments(annotation.maxArguments(), annotation.minArguments(), annotation.arguments()),
                method, instance);
    }


    static List<Argument> createArguments(final int maxArguments, final int minArguments, final String[] names) {
        final List<Argument> arguments = new ArrayList<Argument>();

        for (int i = 0; i < maxArguments; i++) {
            arguments.add(new Argument((i < names.length ? names[i] : "param" + i), i >= minArguments));
        }

        return arguments;
    }

    public static int parseInt(final String s) {
        if (s == null)
            throw new NumberFormatException("Null string");

        // Check for a sign.
        int num = 0;
        int sign = -1;
        final int len = s.length();
        final char ch = s.charAt(0);
        if (ch == '-') {
            if (len == 1)
                throw new NumberFormatException("Missing digits:  " + s);
            sign = 1;
        } else {
            final int d = ch - '0';
            if (d < 0 || d > 9)
                throw new NumberFormatException("Malformed:  " + s);
            num = -d;
        }

        // Build the number.
        final int max = (sign == -1) ?
                -Integer.MAX_VALUE : Integer.MIN_VALUE;
        final int multmax = max / 10;
        int i = 1;
        while (i < len) {
            int d = s.charAt(i++) - '0';
            if (d < 0 || d > 9)
                throw new NumberFormatException("Malformed:  " + s);
            if (num < multmax)
                throw new NumberFormatException("Over/underflow:  " + s);
            num *= 10;
            if (num < (max + d))
                throw new NumberFormatException("Over/underflow:  " + s);
            num -= d;
        }

        return sign * num;
    }


    private static String[] removeUntil(String[] original, int until) {
        String[] newArray = new String[original.length - until];
        System.arraycopy(original, until,       // from array[removeEnd]
                newArray, 0,                    // to array[removeStart]
                newArray.length);       // this number of elements
        return newArray;
    }


    private static double fuzzyEqualsString(String a, String b) {
        return 1.0 - (double) StringUtils.getLevenshteinDistance(a, b) / (a.length() >= b.length() ? a.length() : b.length());
    }
}
