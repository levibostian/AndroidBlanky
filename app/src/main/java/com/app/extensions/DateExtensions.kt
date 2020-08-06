package com.app.extensions

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

val Date.tomorrow: Date
    get() {
        return Calendar.getInstance().apply {
            add(Calendar.DATE, 1)
        }.time
    }

val Date.yesterday: Date
    get() {
        return Calendar.getInstance().apply {
            add(Calendar.DATE, -1)
        }.time
    }

val Date.isoString: String
    get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US).format(this)

fun Date.hoursInFuture(hours: Int): Date = Calendar.getInstance().apply {
    add(Calendar.HOUR, hours)
    add(Calendar.SECOND, 1) // add a little bit or some tests may fail because you expected a date to be 2 hours ahead but it's 1 hour 59 seconds and X milliseconds.
}.time

fun Date.daysAgo(days: Int): Date = Calendar.getInstance().apply {
    add(Calendar.SECOND, -1) // add a little bit or some tests may fail because you expected a date to be 2 hours ahead but it's 1 hour 59 seconds and X milliseconds.
    add(Calendar.DATE, -days)
}.time

/**
 * "3 sec ago"
 * "34 mins ago"
 *
 * if it's in the future...
 * "in 3 min"
 *
 * Remove FORMAT_ABBREV_RELATIVE flag parameter to remove the abbreviation "mins" instead of "minutes"
 *
 * Min resolution of 0 means that all dates matter. Seconds will be reported at "3 secs ago" instead of "0 min ago"
 */
val Date.humanReadableTimeAgoSince: String
    get() = DateUtils.getRelativeTimeSpanString(time, Date().time, 0 /*, DateUtils.FORMAT_ABBREV_RELATIVE */).toString().toLowerCase(Locale.getDefault()).replace(".", "")
