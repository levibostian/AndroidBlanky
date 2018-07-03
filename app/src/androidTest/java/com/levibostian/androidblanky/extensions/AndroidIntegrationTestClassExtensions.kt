package com.levibostian.androidblanky.extensions

import com.levibostian.androidblanky.AndroidIntegrationTestClass
import tools.fastlane.screengrab.Screengrab
import java.io.IOException

fun AndroidIntegrationTestClass.screenshot(name: String) {
    try {
        Screengrab.screenshot(name)
    } catch (e: RuntimeException) {
        // We are running on Android N+. We don't have permission to take screenshot unless we run `fastlane screengrab` from the command line. Sometimes, I don't want to do that.
    }
}