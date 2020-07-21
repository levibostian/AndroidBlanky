package com.app

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import com.app.view.ui.TestMainApplication
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy

class AndroidTestTestRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
    }

    override fun onStart() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())

        super.onStart()
    }

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestMainApplication::class.java.name, context)
    }
}
