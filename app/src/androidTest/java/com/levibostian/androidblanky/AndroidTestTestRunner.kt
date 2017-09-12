package com.levibostian.androidblanky

import android.os.PowerManager.ON_AFTER_RELEASE
import android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP
import android.os.PowerManager.FULL_WAKE_LOCK
import android.content.Context.POWER_SERVICE
import android.os.PowerManager
import android.content.Context.KEYGUARD_SERVICE
import android.app.KeyguardManager
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner
import android.content.Intent
import android.os.Bundle
import com.levibostian.androidblanky.view.ui.TestMainApplication
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy

open class AndroidTestTestRunner : AndroidJUnitRunner() {

    override fun onStart() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())

        val app = targetContext.applicationContext
        cleanupStatusBar(app)

        super.onStart()
    }

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestMainApplication::class.java.name, context)
    }

    private fun cleanupStatusBar(context: Context) {
        val extras = Bundle()
        extras.putBoolean("enabled", true)
        val intent = Intent("com.emmaguy.cleanstatusbar.TOGGLE")
        intent.putExtras(extras)
        context.sendBroadcast(intent)
    }

}