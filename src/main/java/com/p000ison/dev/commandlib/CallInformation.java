package com.p000ison.dev.commandlib;

import java.util.Arrays;

/**
 * Represents a CallInformation
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

    public String getIdentifier() {
        return identifier;
    }

    public String[] getArguments() {
        return arguments;
    }

    public CommandSender getSender() {
        return sender;
    }

    public Command getCommand() {
        return command;
    }

    public void reply(String message) {
        getSender().sendMessage(message);
    }

    public int getPage(int elements) {
        return getPage(elements, executor.getDefaultElementsPerPage());
    }

    public int getPage(int elements, int elementsPerPage) {
        for (int i = 0; i < command.getArguments().size(); i++) {
            Argument argument = command.getArguments().get(i);
            if (argument.isPage()) {
                int page = getInteger(i);

                if (page != -1) {
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

                break;
            }
        }
        return 0;
    }

    public int getStartIndex(int page, int elements) {
        return getStartIndex(page, elements, executor.getDefaultElementsPerPage());
    }

    public int getStartIndex(int elements) {
        int defaultElementsPerPage = executor.getDefaultElementsPerPage();
        int page = getPage(elements, defaultElementsPerPage);

        return getStartIndex(page, elements, defaultElementsPerPage);
    }

    public int getStartIndex(int page, int elements, int elementsPerPage) {
        int start = page * elementsPerPage;

        //check if start index is in bounds
        if (start > elements) {
            start = elements;
        }

        return start;
    }

    public int getEndIndex(int elements) {
        int defaultElementsPerPage = executor.getDefaultElementsPerPage();
        int page = getPage(elements, defaultElementsPerPage);

        return getEndIndex(page, elements, defaultElementsPerPage);
    }

    public int getEndIndex(int page, int elements) {
        return getEndIndex(page, elements, executor.getDefaultElementsPerPage());
    }

    public int getEndIndex(int page, int elements, int elementsPerPage) {
        int start = page * elementsPerPage;
        int end = start + elementsPerPage;

        //check if end index is in bounds
        if (end > elements) {
            end = elements;
        }

        return end;
    }

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
