package org.p000ison.dev.commandlib.test;

import org.p000ison.dev.commandlib.*;

/**
 * Represents a Test
 */
public class Test {

    public static void main(String[] args) {
        CommandExecutor executor = new CommandExecutor() {

            @Override
            public void displayCommandHelp(CommandSender sender, Command command) {

                System.out.println("HELP");
            }
        };
        Command sub = executor.build().setName("sub-test").setUsage("default").addIdentifier("abc").finish(new Command());
        executor.register(executor.build().setName("test").setUsage("default").addIdentifier("test").addSubCommand(sub).finish(new Command()));
        executor.register(sub);

        executor.register(Test.class);


        executor.executeAll(new ConsoleCommandSender(), "gh");
    }


    @CommandAnnotation(name = "test", usage = "default", identifiers = "gh", minArguments = 0, maxArguments = 0)
    public static void test(CommandSender test, CallInformation info) {
        System.out.println("fucake");
    }
}
