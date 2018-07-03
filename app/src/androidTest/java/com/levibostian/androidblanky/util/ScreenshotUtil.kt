package com.levibostian.androidblanky.util

import android.app.Activity
import androidx.test.InstrumentationRegistry
import com.levibostian.androidblanky.AndroidIntegrationTestClass
import com.squareup.spoon.SpoonRule
import tools.fastlane.screengrab.Screengrab

class ScreenshotUtil(val activity: Activity, val spoon: SpoonRule) {

    fun take(name: String) {
        try {
            Screengrab.screenshot(name)
            spoon.screenshot(activity, name)
        } catch (e: RuntimeException) {
            // We are running on Android N+. We don't have permission to take screenshot unless we run `fastlane screengrab` from the command line. Sometimes, I don't want to do that.
        }
    }

}