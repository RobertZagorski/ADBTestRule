package com.rzagorski.adbtestrule.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Robert Zagórski on 2016-10-25.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface PermissionRule {
    GrantPermission[] permissions() default {};
}
