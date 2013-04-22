package org.p000ison.dev.commandapi.test;

import org.p000ison.dev.commandapi.*;

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
        Command sub = executor.build().setName("sub-test").setUsage("default").addIdentifier("abc").finish(new TestSub());
        executor.register(executor.build().setName("test").setUsage("default").addIdentifier("test").addSubCommand(sub).finish(new Command()));
        executor.register(sub);


        executor.executeAll(new ConsoleCommandSender(), "test abc");
    }
}
