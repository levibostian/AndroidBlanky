package com.levibostian.androidblanky.rule

import android.os.Environment
import androidx.test.uiautomator.UiDevice
import java.nio.file.Files.exists
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import androidx.test.InstrumentationRegistry
import androidx.test.InstrumentationRegistry.getTargetContext
import com.levibostian.androidblanky.dev.test.BuildConfig
import org.junit.rules.TestWatcher
import org.junit.rules.TestRule
import org.junit.Rule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.File

object ScreenshotOnErrorRule {

    fun getRule(): TestWatcher {
        return object : TestWatcher() {
            override fun failed(e: Throwable?, description: Description?) {
                // Save to external storage (usually /sdcard/screenshots)
                val path = File("${Environment.getExternalStorageDirectory().absolutePath}/screenshots/${getTargetContext().packageName}")
                if (!path.exists()) {
                    path.mkdirs()
                }

                // Take advantage of UiAutomator screenshot method
                val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                val filename = description!!.className + "-" + description.methodName + ".png"
                val filePath = File(path, filename)
                device.takeScreenshot(filePath)

                Log.d(BuildConfig.APPLICATION_ID, "Saved screenshot as test failed.")
                Log.d(BuildConfig.APPLICATION_ID, "View the screenshot with command: `adb pull $filePath /tmp` to copy the image from your test device into the /tmp directory.")
            }
        }
    }

}