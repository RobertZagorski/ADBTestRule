package com.rzagorski.adbtestrule.annotations.base;

import com.rzagorski.adbtestrule.commands.ADBCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Robert Zagórski on 2016-10-27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Inherited
public @interface ExecutionDetails {

    Class<? extends ADBCommand> executionClass();
}
