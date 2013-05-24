package com.p000ison.dev.commandlib;

/**
 * Represents a Argument
 */
public final class Argument {

    private final String name;
    private final int position;

    private final boolean optional;
    private final boolean decimal;
    private final boolean integer;
    private final boolean page;

    public Argument(String name, int position, boolean optional, boolean decimal,
                    boolean integer, boolean page) {
        this.name = name;
        this.position = position;
        this.optional = optional;
        this.decimal = decimal;
        this.integer = integer;
        this.page = page;
    }

    public Argument(String name, int position) {
        this(name, position, false, false, false, false);
    }

    public Argument(String name, int position, boolean optional) {
        this(name, position, optional, false, false, false);
    }

    public Argument(String name, int position, boolean optional, boolean page) {
        this(name, position, optional, false, false, page);
    }

    public final String getName() {
        return name;
    }

    public final int getPosition() {
        return position;
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
                ", position=" + position +
                ", optional=" + optional +
                ", decimal=" + decimal +
                ", integer=" + integer +
                '}';
    }
}
