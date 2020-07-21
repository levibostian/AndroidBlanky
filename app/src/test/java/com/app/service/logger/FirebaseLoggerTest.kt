package com.app.service.logger

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.UnitTest
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseLoggerTest : UnitTest() {

    private lateinit var logger: FirebaseLogger

    @Before
    override fun setup() {
        super.setup()

        logger = FirebaseLogger(context)
    }

    @Test
    fun `UserPropertyKey extensions firebaseName - expect name to be correct format`() {
        val given = UserPropertyKey.HighlightValue
        val expected = "highlight_value"

        val actual = given.firebaseName

        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun `ActivityEventParamKey extensions firebaseName - expect name to be correct format`() {
        val given = ActivityEventParamKey.PaidUser
        val expected = "paid_user"

        val actual = given.firebaseName

        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun `ActivityEvent extensions firebaseName - expect name to be correct format`() {
        val given = ActivityEvent.OpenedDynamicLink
        val expected = "opened_dynamic_link"

        val actual = given.firebaseName

        assertThat(expected).isEqualTo(actual)
    }
}
