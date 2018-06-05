package com.levibostian.androidblanky.view.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.levibostian.androidblanky.service.manager.NotificationChannelManager
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.view.ui.MainApplication
import javax.inject.Inject

class LaunchActivity: Activity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, LaunchActivity::class.java)
        }
    }

    @Inject lateinit var userManager: UserManager
    @Inject lateinit var notificationChannelManager: NotificationChannelManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MainApplication).component.inject(this)

        notificationChannelManager.createChannels()

        if (userManager.isUserLoggedIn()) {
            startActivity(MainActivity.getIntent(this))
            finish()
        } else {
            // For demo purposes, we are going to "log in" the user here. You are going to want to remove this.
            userManager.id = "1"
            startActivity(MainActivity.getIntent(this))
            finish()
        }
    }

}