package com.rzagorski.adbtestrule.commands;

import java.lang.annotation.Annotation;

/**
 * Created by Robert Zagórski on 2016-10-25.
 */

public abstract class ADBCommand<T extends Annotation> {

    public ADBCommand(T annotation) {
    }

    public abstract void execute();
}
