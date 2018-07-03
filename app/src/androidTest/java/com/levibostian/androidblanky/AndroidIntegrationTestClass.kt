package com.levibostian.androidblanky

import android.app.Instrumentation
import androidx.test.uiautomator.UiDevice

interface AndroidIntegrationTestClass {
    fun getInstrumentation(): Instrumentation
}

fun AndroidIntegrationTestClass.orientationPortrait() {
    val device = UiDevice.getInstance(getInstrumentation())
    device.setOrientationNatural()
}

fun AndroidIntegrationTestClass.orientationLandscape() {
    val device = UiDevice.getInstance(getInstrumentation())
    device.setOrientationLeft()
}