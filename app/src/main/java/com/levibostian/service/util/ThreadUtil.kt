package com.levibostian.service.util

import android.os.Looper

object ThreadUtil {

    val isMainThread: Boolean
        get() = Looper.myLooper() == Looper.getMainLooper()

    val isBackgroundThread: Boolean
        get() = !isMainThread
}