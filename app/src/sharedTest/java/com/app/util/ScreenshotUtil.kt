package com.app.util

import android.app.Activity
import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter
import tools.fastlane.screengrab.Screengrab
import java.lang.Thread.sleep

class ScreenshotUtil(val activity: Activity) {

    // Firebase test lab: https://firebase.google.com/docs/test-lab/android/test-screenshots
    fun take(name: String) {
        name.replace(' ', '_').let { cleanedName ->
            // Firebase screenshots
            ScreenShotter.takeScreenshot(name, activity)
        }
    }

    // Fastlane screengrab for store pics
    fun takeForStore(name: String) {
        sleep(300) // Assert that the view is refreshed enough to take pic.

        name.replace(' ', '_').let { cleanedName ->
            try {
                Screengrab.screenshot(cleanedName)
            } catch (e: Throwable) {
                // in case we are running this test *not* using screengrab but instead instrumentation tests, the screenshot attempt will fail.
            }
        }
    }
}
