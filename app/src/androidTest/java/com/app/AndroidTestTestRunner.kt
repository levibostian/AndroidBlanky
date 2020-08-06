package com.app

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy

class AndroidTestTestRunner : AndroidJUnitRunner() {

    override fun onStart() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())

        super.onStart()
    }

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
