package com.rzagorski.adbtestrule.annotations;

import com.rzagorski.adbtestrule.annotations.base.ADBOperation;
import com.rzagorski.adbtestrule.annotations.base.ExecutionDetails;
import com.rzagorski.adbtestrule.commands.GrantPermissionCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Robert Zagórski on 2016-10-25.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ADBOperation
@ExecutionDetails(
        executionClass = GrantPermissionCommand.class
)
public @interface GrantPermission {
    String value();
}
