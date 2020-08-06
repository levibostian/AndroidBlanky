package com.app.util

import android.app.Activity
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter
import tools.fastlane.screengrab.Screengrab
import java.lang.Thread.sleep

class ScreenshotUtil(val activity: Activity) {

    // We know screengrab is running if we only run tests with a certain annotation.
    // See: https://docs.fastlane.tools/actions/screengrab/#advanced-screengrab to learn more about `launch_arguments` argument for `screengrab` where we can pass in args.
    val isScreenGrabRunning: Boolean
        get() = InstrumentationRegistry.getArguments().getString("annotation") != null

    fun take(name: String) {
        if (isScreenGrabRunning) takeForStore(name)
        else takeForTestDebugging(name)
    }

    // Firebase test lab: https://firebase.google.com/docs/test-lab/android/test-screenshots
    private fun takeForTestDebugging(name: String) {
        name.replace(' ', '_').let { cleanedName ->
            // Firebase screenshots
            ScreenShotter.takeScreenshot(name, activity)
        }
    }

    // Fastlane screengrab for store pics
    private fun takeForStore(name: String) {
        sleep(1000) // Assert that the view is refreshed enough to take pic.

        name.replace(' ', '_').let { cleanedName ->
            Screengrab.screenshot(cleanedName)
        }
    }
}
