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

    String name() default "";

    String usage() default "";

    String[] identifiers() default {};

    String[] aliases() default {};

    String[] permissions() default {};

    int minArguments() default 0;

    int maxArguments() default 0;

    String[] arguments() default {};
}
