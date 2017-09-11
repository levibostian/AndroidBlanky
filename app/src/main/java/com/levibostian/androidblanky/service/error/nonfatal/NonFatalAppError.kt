package com.levibostian.androidblanky.service.error.nonfatal

/**
 * Error caught by RxJava and provided to onError().
 */
abstract class NonFatalAppError(message: String): Exception(message)