package com.app.service.manager

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.UnitTest
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserManagerTest : UnitTest() {

    private lateinit var manager: UserManager

    @Before
    override fun setup() {
        super.setup()

        /**
         * Convert this test into a robolectric test so that we can use a robo version of shared prefs to test this test in an integration way
         */

        manager = UserManager(keyValueStorage)
    }

    @Test
    fun authToken_getNullNotSet() {
        assertThat(manager.id).isNull()
    }

    @Test
    fun authToken_givenAuthToken_expectGetIt() {
        val givenToken = "access-token"

        manager.authToken = givenToken

        assertThat(manager.authToken).isEqualTo(givenToken)
    }

    // logout

    @Test
    fun `logout - expect delete properties`() {
        manager.id = 1
        manager.authToken = "test"

        manager.logout()

        assertThat(manager.id).isNull()
        assertThat(manager.authToken).isNull()
    }

    // isUserLoggedIn

    @Test
    fun `isUserLoggedIn - given no data written to manager yet, expect false`() {
        assertThat(manager.isUserLoggedIn).isFalse()
    }

    @Test
    fun `isUserLoggedIn - given id saved, expect false`() {
        manager.id = 1

        assertThat(manager.isUserLoggedIn).isFalse()
    }

    @Test
    fun `isUserLoggedIn - given auth saved, expect false`() {
        manager.authToken = "123"

        assertThat(manager.isUserLoggedIn).isFalse()
    }

    @Test
    fun `isUserLoggedIn - given all properties have a value, expect true`() {
        manager.id = 1
        manager.authToken = "123"

        assertThat(manager.isUserLoggedIn).isTrue()
    }
}
