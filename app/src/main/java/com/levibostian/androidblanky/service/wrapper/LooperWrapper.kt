package com.levibostian.androidblanky.service.wrapper

import android.os.Looper

open class LooperWrapper {

    open fun isOnUIThread(): Boolean = Looper.getMainLooper().thread != Thread.currentThread()

}