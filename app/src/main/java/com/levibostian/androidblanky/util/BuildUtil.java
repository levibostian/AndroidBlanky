package com.levibostian.androidblanky.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class BuildUtil {

    public static final int INVALID_VERSION_CODE = -1;

    public static String getVersionName(Context context) {
        String versionName;

        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "Unknown";
        }

        return versionName;
    }

    public static int getVersionCode(Context context) {
        int versionCode;

        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            versionCode = INVALID_VERSION_CODE;
        }

        return versionCode;
    }

    public static String getApplicationName(Context context) {
        int stringId = context.getApplicationInfo().labelRes;

        return context.getString(stringId);
    }

}
