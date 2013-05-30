package com.p000ison.dev.commandlib;

import java.util.Arrays;

/**
 * A CallInformation contains information of a call event. A new instance is created every time a command is called.
 */
public class CallInformation {

    private final String identifier;
    private final String[] arguments;
    private final Command command;
    private final CommandSender sender;

    private final CommandExecutor executor;

    protected CallInformation(CommandExecutor executor, Command command, CommandSender sender, String identifier, String[] arguments) {
        this.identifier = identifier;
        this.arguments = arguments;
        this.command = command;
        this.sender = sender;
        this.executor = executor;
    }

    /**
     * Gets the identifier which was used to call the {@link Command}
     *
     * @return The identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Gets the arguments which were used to call the {@link Command}
     *
     * @return The identifier
     * @see Argument
     */
    public String[] getArguments() {
        return arguments;
    }

    /**
     * Gets the {@link CommandSender} which who called the {@link Command}
     *
     * @return The identifier
     * @see CommandSender
     */
    public CommandSender getSender() {
        return sender;
    }

    /**
     * Gets the {@link Command} who was called.
     *
     * @return The identifier
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Relies the {@link CommandSender} who called this{@link Command}
     */
    public void reply(String message) {
        getSender().sendMessage(message);
    }

    /**
     * The max elements per page are here {@link com.p000ison.dev.commandlib.CommandExecutor#getDefaultElementsPerPage()}
     *
     * @see #getPage(int, int)
     */
    public int getPage(int elements) {
        return getPage(elements, executor.getDefaultElementsPerPage());
    }

    /**
     * @return Whether there is an arguments which takes a page as {@link Argument}
     */
    public boolean hasPage() {
        for (Argument argument : command.getArguments()) {
            if (argument.isPage()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the page if one was supplied by an {@link Argument}. If the page is higher than there are elements
     * it will return the last page.
     *
     * @param elements The max number of elements
     * @return The page, -1 if there is no {@link Argument} which takes a page
     */
    public int getPage(int elements, int elementsPerPage) {
        for (int i = 0; i < command.getArguments().size(); i++) {
            Argument argument = command.getArguments().get(i);
            if (argument.isPage()) {

                //too many arguments
                if (i >= getArguments().length) {
                    return 0;
                }

                int page = getInteger(i);

                //we failed at parsing the page
                if (page == -1) {
                    return -1;
                }

                int numPages = elements / elementsPerPage;

                if (elements % elementsPerPage != 0) {
                    numPages++;
                }

                if (page >= numPages) {
                    page = numPages;
                } else if (page < 0) {
                    page = 0;
                }

                if (page > 0) {
                    page--;
                }

                return page;
            }
        }
        return 0;
    }

    /**
     * @see #getStartIndex(int, int, int)
     */
    public int getStartIndex(int page, int elements) {
        return getStartIndex(page, elements, executor.getDefaultElementsPerPage());
    }

    /**
     * Here elementsPerPage is {@link com.p000ison.dev.commandlib.CommandExecutor#getDefaultElementsPerPage()}
     *
     * @see #getStartIndex(int, int, int)
     */
    public int getStartIndex(int elements) {
        int defaultElementsPerPage = executor.getDefaultElementsPerPage();
        int page = getPage(elements, defaultElementsPerPage);

        return getStartIndex(page, elements, defaultElementsPerPage);
    }

    /**
     * If you want to display multiple elements over multiple pages this method returns you the start index.
     *
     * @param page            The page to use
     * @param elements        The amount of elements you want to display
     * @param elementsPerPage The max elements per page
     * @return The start index
     */
    public int getStartIndex(int page, int elements, int elementsPerPage) {
        int start = page * elementsPerPage;

        //check if start index is in bounds
        if (start > elements) {
            start = elements;
        }

        return start;
    }

    /**
     * Same methods as the {@link #getStartIndex(int, int, int)} methods.
     *
     * @see #getStartIndex(int, int, int)
     */
    public int getEndIndex(int elements) {
        int defaultElementsPerPage = executor.getDefaultElementsPerPage();
        int page = getPage(elements, defaultElementsPerPage);

        return getEndIndex(page, elements, defaultElementsPerPage);
    }

    /**
     * Same methods as the {@link #getStartIndex(int, int, int)} methods.
     *
     * @see #getStartIndex(int, int, int)
     */
    public int getEndIndex(int page, int elements) {
        return getEndIndex(page, elements, executor.getDefaultElementsPerPage());
    }

    /**
     * Same methods as the {@link #getStartIndex(int, int, int)} methods.
     *
     * @see #getStartIndex(int, int, int)
     */
    public int getEndIndex(int page, int elements, int elementsPerPage) {
        int start = page * elementsPerPage;
        int end = start + elementsPerPage;

        //check if end index is in bounds
        if (end > elements) {
            end = elements;
        }

        return end;
    }

    /**
     * Parses the argument n and returns it.
     *
     * @param index The index
     * @return The n argument as integer.
     */
    public int getInteger(int index) {
        try {
            return CommandExecutor.parseInt(getArguments()[index]);
        } catch (NumberFormatException e) {
            return -1;
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "CallInformation{" +
                "identifier='" + identifier + '\'' +
                ", arguments=" + Arrays.toString(arguments) +
                ", sender=" + sender +
                '}';
    }
}
