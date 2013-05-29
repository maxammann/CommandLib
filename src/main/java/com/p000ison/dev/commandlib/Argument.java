package com.p000ison.dev.commandlib;

/**
 * Represents a Argument
 */
public final class Argument {

    private final String name;

    private final boolean optional;
    private final boolean decimal;
    private final boolean integer;
    private final boolean page;

    private Argument(String name, boolean optional, boolean decimal,
                     boolean integer, boolean page) {
        this.name = name;
        this.optional = optional;
        this.decimal = decimal;
        this.integer = integer;
        this.page = page;
    }

    /**
     * Constructs a new argument
     *
     * @param name The name of this argument. Can be used to display for example the help of a command.
     */
    public Argument(String name) {
        this(name, false, false, false, false);
    }

    /**
     * Constructs a new argument
     *
     * @param name     The name of this argument. Can be used to display for example the help of a command.
     * @param optional Whether this argument is optional
     */
    public Argument(String name, boolean optional) {
        this(name, optional, false, false, false);
    }

    /**
     * Constructs a new argument
     *
     * @param name     The name of this argument. Can be used to display for example the help of a command.
     * @param optional Whether this argument is optional
     * @param page     Whether this argument contains the page of this command
     */
    public Argument(String name, boolean optional, boolean page) {
        this(name, optional, false, false, page);
    }


    /**
     * Gets the name of this argument
     *
     * @return The name of this argument
     */
    public final String getName() {
        return name;
    }


    /**
     * @return Whether this argument is required
     */
    public final boolean isRequired() {
        return !optional;
    }

    public final boolean isDecimal() {
        return decimal;
    }

    public final boolean isInteger() {
        return integer;
    }

    /**
     * @return Whether this argument contains the page of this command
     */
    public final boolean isPage() {
        return page;
    }

    @Override
    public String toString() {
        return "Argument{" +
                "name='" + name + '\'' +
                ", optional=" + optional +
                ", decimal=" + decimal +
                ", integer=" + integer +
                '}';
    }
}
