package com.p000ison.dev.commandlib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface CommandHandler {

    String name();

    String usage();

    String[] identifiers();

    String[] aliases() default {};

    String[] permissions() default {};

    int minArguments();

    int maxArguments();

    String[] arguments() default {};
}
