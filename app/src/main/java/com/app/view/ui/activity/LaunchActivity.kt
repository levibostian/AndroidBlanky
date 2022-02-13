package com.app.view.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.service.logger.Logger
import com.app.service.manager.NotificationChannelManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {

    @Inject lateinit var notificationChannelManager: NotificationChannelManager
    @Inject lateinit var logger: Logger

    private val LOGIN_ACTIVITY_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationChannelManager.createChannels()
    }

    private fun goToMainPartOfApp() {
        // startActivity(MainActivity.getIntent(this))

        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LOGIN_ACTIVITY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    goToMainPartOfApp()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
