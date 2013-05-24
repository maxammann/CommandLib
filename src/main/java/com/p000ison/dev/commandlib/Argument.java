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

    public Argument(String name, boolean optional, boolean decimal,
                    boolean integer, boolean page) {
        this.name = name;
        this.optional = optional;
        this.decimal = decimal;
        this.integer = integer;
        this.page = page;
    }

    public Argument(String name) {
        this(name, false, false, false, false);
    }

    public Argument(String name, boolean optional) {
        this(name, optional, false, false, false);
    }

    public Argument(String name,  boolean optional, boolean page) {
        this(name, optional, false, false, page);
    }

    public final String getName() {
        return name;
    }

    public final boolean isRequired() {
        return !optional;
    }

    public final boolean isDecimal() {
        return decimal;
    }

    public final boolean isInteger() {
        return integer;
    }

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
