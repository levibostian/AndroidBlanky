package com.levibostian.androidblanky.view.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.fragment.app.transaction
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.DataDestroyer
import com.levibostian.androidblanky.service.event.LogoutUserEvent
import com.levibostian.androidblanky.service.logger.Logger
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.util.InstallReferrerProcessor
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import kotlinx.android.synthetic.main.activity_toolbar_fragment_container.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

/**
 * Extend for all root activities.
 *
 * Note: User must be logged in to use this Activity.
 */
abstract class BaseActivity: AppCompatActivity() {

    @Inject lateinit var userManager: UserManager
    @Inject lateinit var installReferrerProcessor: InstallReferrerProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!userManager.isUserLoggedIn()) throw RuntimeException("Activity cannot run when user is not logged in")

        // I am capturing the referrer here instead of in LaunchActivity because (1) The user is logged into the app at this point and Firebase Analytics will then be associated with a user id. (2) I should only really care about where users came from that successfully logged into the app as they are users who want to use the product.
        installReferrerProcessor.process(this)
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)

        // Users can remove accounts in the settings app on the device. Check if they did while the app was in the background.
        if (!userManager.isUserLoggedIn()) {
            EventBus.getDefault().post(LogoutUserEvent())
        }
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: LogoutUserEvent) {
        startActivity(LaunchActivity.getIntent(this, true))
        finish()
    }

}