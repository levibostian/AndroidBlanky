package com.levibostian.androidblanky

import android.os.PowerManager.ON_AFTER_RELEASE
import android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP
import android.os.PowerManager.FULL_WAKE_LOCK
import android.content.Context.POWER_SERVICE
import android.os.PowerManager
import android.content.Context.KEYGUARD_SERVICE
import android.app.KeyguardManager
import android.annotation.SuppressLint
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner
import android.content.Intent
import android.os.Bundle

open class AndroidTestTestRunner : AndroidJUnitRunner() {

    override fun onStart() {
        val app = targetContext.applicationContext
        cleanupStatusBar(app)

        super.onStart()
    }

    private fun cleanupStatusBar(context: Context) {
        val extras = Bundle()
        extras.putBoolean("enabled", true)
        val intent = Intent("com.emmaguy.cleanstatusbar.TOGGLE")
        intent.putExtras(extras)
        context.sendBroadcast(intent)
    }

}