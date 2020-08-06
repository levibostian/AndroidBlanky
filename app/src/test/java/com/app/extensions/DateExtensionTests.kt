package com.app.extensions

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.UnitTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DateExtensionTests : UnitTest() {

    @Test
    fun `humanReadableTimeAgoSince - given time in the past, expect human readable time`() {
        val given = Date().daysAgo(2)

        val actual = given.humanReadableTimeAgoSince

        assertThat(actual).isEqualTo("2 days ago")
    }

    @Test
    fun `humanReadableTimeAgoSince - given time in the future, expect human readable time`() {
        val given = Date().hoursInFuture(5)

        val actual = given.humanReadableTimeAgoSince

        assertThat(actual).isEqualTo("in 5 hours")
    }
}
