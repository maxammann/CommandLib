import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.p000ison.dev.commandlib.*;
import com.p000ison.dev.commandlib.commands.HelpCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * Represents a CommandTest
 */
@RunWith(JUnit4.class)
public class CommandTest {
    private static final String HELP_TEXT = "| This is a help text! |";
    private static final String COMMAND_NOT_FOUND = "| Command not found! |";
    private static final String TEST_TEXT = "You executed the test command!";
    private static final String TEST_TEXT_SUB = "You executed the sub test command!";
    private static final String TEST_TEXT_SUB_SUB = "You executed the sub sub test command!";

    private CommandExecutor executor;
    private ConsoleCommandSender consoleSender;
    private ByteArrayOutputStream outContent;

    @Before
    public void createCommands() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        consoleSender = new ConsoleCommandSender();

        executor = new CommandExecutor() {
            @Override
            public void onPreCommand(CallInformation info) {
            }

            @Override
            public void onExecutionBlocked(CommandSender sender, Command command) {
            }

            @Override
            public void onPostCommand(CallInformation info) {
            }

            @Override
            public void onDisplayCommandHelp(CommandSender sender, Command command) {
                sender.sendMessage(HELP_TEXT);
            }

            @Override
            public void onCommandNotFound(CommandSender sender) {
                sender.sendMessage(COMMAND_NOT_FOUND);
            }

            @Override
            public void onPermissionFailed(CommandSender sender, Command command) {
            }

            @Override
            public void onPermissionFailed(CommandSender sender) {
            }
        };

        Command subCommand = executor.build(this, "SubTest");
        Command subsubCommand = executor.build(this, "SubSubTest");
        executor.register(this, "Test").addSubCommand(subCommand);
        subCommand.addSubCommand(subsubCommand);
        executor.register(new HelpCommand(executor, "Help", "helpme", "help", "help", "ids-args-usage"));
    }

    public static void main(String[] args) {
        ConsoleCommandSender consoleSender = new ConsoleCommandSender();

        CommandExecutor executor = new CommandExecutor() {
            @Override
            public void onPreCommand(CallInformation info) {
            }

            @Override
            public void onExecutionBlocked(CommandSender sender, Command command) {
            }

            @Override
            public void onPostCommand(CallInformation info) {
            }

            @Override
            public void onDisplayCommandHelp(CommandSender sender, Command command) {
                sender.sendMessage("help " + command.getName());
            }

            @Override
            public void onCommandNotFound(CommandSender sender) {
                sender.sendMessage("command not found");
            }

            @Override
            public void onPermissionFailed(CommandSender sender, Command command) {
                sender.sendMessage("no permission");
            }

            @Override
            public void onPermissionFailed(CommandSender sender) {
            }
        };

        executor.register(new Command() {
            @Override
            public void execute(CommandSender sender, CallInformation information) {
                information.reply("cmd2 " + information);
            }
        }.setName("CMD2").setUsage("usage2").setIdentifiers("cmd")
                .addArgument(new Argument("arg1", false)));

        executor.register(new Command() {
            @Override
            public void execute(CommandSender sender, CallInformation information) {
                information.reply("cmd1 " + information);
            }
        }.setName("CMD1").setUsage("usage1").setIdentifiers("cmd")
                .addArgument(new Argument("arg1", false))
                .addArgument(new Argument("page", false)));


        executor.executeAll(consoleSender, "cmd");
    }

    @Test
    public void testMainCommand() {
        executor.executeAll(consoleSender, "test hey");
        assertEquals(TEST_TEXT, outContent.toString().trim());
    }

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();

    @Test
    public void testSubCommand() {
        executor.executeAll(consoleSender, "test sub");
        assertEquals(TEST_TEXT_SUB, outContent.toString().trim());

    }

    @Test
    public void testSubSubCommand() {
        executor.executeAll(consoleSender, "test sub subsub");
        assertEquals(TEST_TEXT_SUB_SUB, outContent.toString().trim());
    }

    @After
    public void clean() {
        outContent.reset();
    }

    @CommandHandler(name = "Test", usage = "None", identifiers = {"test"}, arguments = "test", minArguments = 1, maxArguments = 1)
    public void testCommand(CommandSender sender, CallInformation info) {
        info.reply(TEST_TEXT);
    }

    @CommandHandler(name = "SubTest", usage = "None", identifiers = {"sub"}, minArguments = 0, maxArguments = 0)
    public void testSubCommand(CommandSender sender, CallInformation info) {
        info.reply(TEST_TEXT_SUB);
    }

    @CommandHandler(name = "SubSubTest", usage = "None", identifiers = {"subsub"}, minArguments = 0, maxArguments = 0)
    public void testSubSubCommand(CommandSender sender, CallInformation info) {
        info.reply(TEST_TEXT_SUB_SUB);
    }

    @After
    public void cleanUpStreams() throws IOException {
        outContent.close();
        System.setOut(null);
    }
}
