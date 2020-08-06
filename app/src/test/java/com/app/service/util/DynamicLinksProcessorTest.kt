package com.app.service.util

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.UnitTest
import com.app.extensions.random
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DynamicLinksProcessorTest : UnitTest() {

    private val linkPrefix = "https://app.ericaziel.com/"

    @Test
    fun `getActionFromDynamicLink - given uri with passwordless token, expect get action`() {
        val givenToken = Int.random.toString()

        val actual = DynamicLinksProcessor.getActionFromDynamicLink(Uri.parse("$linkPrefix?passwordless_token=$givenToken"))

        assertThat(actual).isEqualTo(DynamicLinkAction.PasswordlessTokenExchange(givenToken))
    }

    @Test
    fun `getActionFromDynamicLink - given uri with no params useful, expect get null`() {
        val actual = DynamicLinksProcessor.getActionFromDynamicLink(Uri.parse("$linkPrefix?nothing-useful=123"))

        assertThat(actual).isNull()
    }
}
