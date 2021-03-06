package com.rzagorski.adbtestruleexample;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.rzagorski.adbtestrule.ADBTestRule;
import com.rzagorski.adbtestrule.annotations.AirplaneMode;
import com.rzagorski.adbtestrule.annotations.GrantPermission;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Rule
    public ADBTestRule mADBRule = new ADBTestRule();

    @Test
    @TargetApi(Build.VERSION_CODES.M)
    public void testPermissionIsDeniedAtBeginning() throws Exception {
        mActivityTestRule.launchActivity(null);
        int permissionResult = ContextCompat.checkSelfPermission(mActivityTestRule.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        assertTrue(permissionResult == PackageManager.PERMISSION_DENIED);
    }

    @Test
    @GrantPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    @TargetApi(Build.VERSION_CODES.M)
    public void testPermissionIsGrantedWithAnnotation() throws Exception {
        mActivityTestRule.launchActivity(null);
        int permissionResult = ContextCompat.checkSelfPermission(mActivityTestRule.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        assertTrue(permissionResult == PackageManager.PERMISSION_GRANTED);
    }

    @Test
    @AirplaneMode(true)
    @TargetApi(Build.VERSION_CODES.M)
    public void testAirplaneModeOn() throws Exception {
        mActivityTestRule.launchActivity(null);
        assert true;
    }
}
