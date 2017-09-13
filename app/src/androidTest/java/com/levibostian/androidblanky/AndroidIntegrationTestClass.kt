package com.levibostian.androidblanky

import android.app.Instrumentation
import android.support.test.InstrumentationRegistry
import android.support.test.uiautomator.UiDevice

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