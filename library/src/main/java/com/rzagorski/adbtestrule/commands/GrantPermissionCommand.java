package com.rzagorski.adbtestrule.commands;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;

import com.rzagorski.adbtestrule.Utils;
import com.rzagorski.adbtestrule.annotations.GrantPermission;

import java.io.IOException;

/**
 * Created by Robert Zagórski on 2016-10-25.
 */

public class GrantPermissionCommand<T extends GrantPermission> extends ADBCommand<T> {
    private static final String PERMISSION_COMMAND = "pm grant %s %s";

    /**
     * The current instrumentation of test execution;
     */
    private Instrumentation instrumentation;

    /**
     * Fully qualified permission name
     *
     * @see android.Manifest.permission#CAMERA
     */
    private String permissionName;

    public GrantPermissionCommand(T annotation) {
        super(annotation);
        this.instrumentation = InstrumentationRegistry.getInstrumentation();
        this.permissionName = annotation.value();
    }

    @Override
    public void execute() {
        executePermissionCommand(instrumentation, permissionName);
    }

    private void executePermissionCommand(Instrumentation instrumentation,
                                          String permissionName) {
        String applicationPackage = Utils.getTargetPackageName(instrumentation);
        String command = String.format(PERMISSION_COMMAND, applicationPackage, permissionName);
        try {
            Utils.executeShellCommand(instrumentation, command);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("could not execute grant permission command on connected device", e.getCause());
        }
    }
}
