package com.app.service.logger

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Log the many activities/events that can happen within the app.
 *
 * The functions here are meant to be common between many different types of loggers. This file is meant to be generic enough that all loggers can inherit so there are no specific functions meant for 1 specific logger, as much as we can help it.
 */
class Logger @Inject constructor(@ApplicationContext private val context: Context) {
    private val tag: String = context.getString(R.string.app_name).replace(" ", "_")

    fun verbose(message: String) {
        Log.v(tag, message)
    }

    fun debug(message: String) {
        Log.d(tag, message)
    }
}
