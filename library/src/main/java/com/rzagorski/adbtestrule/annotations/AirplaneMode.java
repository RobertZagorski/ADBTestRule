package com.rzagorski.adbtestrule.annotations;

import com.rzagorski.adbtestrule.annotations.base.ADBOperation;
import com.rzagorski.adbtestrule.annotations.base.ExecutionDetails;
import com.rzagorski.adbtestrule.commands.AirplaneModeCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Robert Zagórski on 2016-11-02.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ADBOperation
@ExecutionDetails(
        executionClass = AirplaneModeCommand.class
)
public @interface AirplaneMode {
    boolean value();
}
