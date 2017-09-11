package com.levibostian.androidblanky.service.error.fatal

/**
 * Error that puts the app in an unstable state. The app dev has the decision to have these errors crash the app or catch them and handle some other way.
 */
abstract class FatalAppError(message: String): Exception(message)