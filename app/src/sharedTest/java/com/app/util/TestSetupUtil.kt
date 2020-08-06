package com.app.util

import android.content.SharedPreferences
import com.app.mock.MockWebServer
import com.app.service.DataDestroyer
import com.app.service.manager.UserManager
import com.app.service.util.ThreadUtil
import com.levibostian.teller.Teller
import javax.inject.Inject

class TestSetupUtil @Inject constructor(
    private val mockWebServer: MockWebServer,
    private val userManager: UserManager,
    private val dataDestroyer: DataDestroyer,
    private val sharedPrefs: SharedPreferences
) {

    /**
     * Run from background thread. Prefer in the startup of your tests.
     */
    fun setup(loginUser: Boolean = false) {
        assert(ThreadUtil.isBackgroundThread)

        mockWebServer.start()

        Teller.initTesting(sharedPrefs)

        dataDestroyer.destroyAllSync()

        if (loginUser) {
            userManager.authToken = "123"
            userManager.id = 1

            assert(userManager.isUserLoggedIn) { "Update the code above for logging in user." }
        }
    }

    fun teardown() {
        mockWebServer.stop()
    }
}
