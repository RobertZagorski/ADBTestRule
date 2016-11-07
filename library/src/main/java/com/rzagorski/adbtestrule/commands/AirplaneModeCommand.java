package com.rzagorski.adbtestrule.commands;

import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.os.Build;
import android.support.test.InstrumentationRegistry;

import com.rzagorski.adbtestrule.Utils;
import com.rzagorski.adbtestrule.annotations.AirplaneMode;

import java.io.IOException;

/**
 * Created by Robert Zagórski on 2016-11-02.
 *
 * @deprecated since {@link android.os.Build.VERSION_CODES#M}
 */
@Deprecated
public class AirplaneModeCommand<T extends AirplaneMode> extends ADBCommand<T> {
    private static final String AIRPLANE_MODE_COMMAND = "settings put global airplane_mode_on %s; am broadcast -a android.intent.action.AIRPLANE_MODE --ez state %s";

    /**
     * The current instrumentation of test execution;
     */
    private Instrumentation instrumentation;

    private boolean annotationValue;

    public AirplaneModeCommand(T annotation) {
        super(annotation);
        this.instrumentation = InstrumentationRegistry.getInstrumentation();
        this.annotationValue = annotation.value();
    }


    @Override
    public void execute() {
        executePermissionCommand(instrumentation, annotationValue);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void executePermissionCommand(Instrumentation instrumentation,
                                          boolean value) {
        String command = String.format(AIRPLANE_MODE_COMMAND, value ? "1" : "0", String.valueOf(value));
        try {
            Utils.executeShellCommand(instrumentation, command);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("could not execute grant permission command on connected device", e.getCause());
        }
    }
}
