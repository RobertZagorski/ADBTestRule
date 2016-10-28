package com.rzagorski.adbtestrule;

import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.os.UserHandle;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Zagórski on 2016-10-25.
 */

public class Utils {

    /**
     * @param instrumentation
     * @param cmd
     * @return
     * @throws IOException
     * @since API Level 21
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String executeShellCommand(Instrumentation instrumentation, String cmd) throws IOException {
        ParcelFileDescriptor pfd = instrumentation.getUiAutomation().executeShellCommand(cmd);
        byte[] buf = new byte[512];
        int bytesRead;
        FileInputStream fis = new ParcelFileDescriptor.AutoCloseInputStream(pfd);
        StringBuffer stdout = new StringBuffer();
        while ((bytesRead = fis.read(buf)) != -1) {
            stdout.append(new String(buf, 0, bytesRead));
        }
        fis.close();
        return stdout.toString();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void revokePermission(Instrumentation instrumentation, String packageName, String permission)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        PackageManager packageManager = instrumentation.getContext().getPackageManager();
        Method method = PackageManager.class.getMethod("revokeRuntimePermission", String.class, String.class, UserHandle.class);
        UserHandle userHandle = android.os.Process.myUserHandle();
        method.invoke(packageManager, packageName, permission, userHandle);
    }

    public static String getTargetPackageName(Instrumentation instrumentation) {
        return instrumentation.getTargetContext().getPackageName();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static List<String> getPackageRuntimePermissions(Instrumentation instrumentation) {
        PackageManager packageManager = instrumentation.getTargetContext().getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(getTargetPackageName(instrumentation), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not obtain target package permissions", e.getCause());
        }
        String[] permissionNames = packageInfo.requestedPermissions;
        int[] permissionInfosFlags = packageInfo.requestedPermissionsFlags;
        List<String> runtimePermissions = new ArrayList<>();
        for (int i = 0; i < permissionNames.length; ++i) {
            if (permissionInfosFlags[i] >= PackageInfo.REQUESTED_PERMISSION_GRANTED) {
                continue;
            }
            runtimePermissions.add(permissionNames[i]);
        }
        return runtimePermissions;
    }
}
