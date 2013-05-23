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

    public Argument(final String name, final int position, final boolean optional, final boolean decimal,
                    final boolean integer, final boolean page) {
        this.name = name;
        this.position = position;
        this.optional = optional;
        this.decimal = decimal;
        this.integer = integer;
        this.page = page;
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
