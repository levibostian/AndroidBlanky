package com.levibostian.androidblanky.util;

import android.util.Log;

public class LogUtil {

    public static final String LOG_TAG = "LOG_NAME_HERE";

    public static void d(String message) {
        Log.d(LOG_TAG, message);
    }

}
