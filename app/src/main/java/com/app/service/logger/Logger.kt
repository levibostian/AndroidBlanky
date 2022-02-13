package com.app.service.logger

import android.os.Bundle
import javax.inject.Inject

/**
 * Log the many activities/events that can happen within the app.
 *
 * The functions here are meant to be common between many different types of loggers. This file is meant to be generic enough that all loggers can inherit so there are no specific functions meant for 1 specific logger, as much as we can help it.
 */
class Logger @Inject constructor() {
    fun verbose(message: String) {

    }

    fun debug(message: String) {

    }
}
