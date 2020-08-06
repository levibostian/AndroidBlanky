package com.app

import kotlin.annotation.Retention

/**
 * Test only exists to take screenshots.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
annotation class ScreenshotOnly
