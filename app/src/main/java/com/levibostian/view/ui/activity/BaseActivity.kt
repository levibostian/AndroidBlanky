package com.levibostian.view.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.fragment.app.transaction
import com.levibostian.R
import com.levibostian.extensions.onCreateDiGraph
import com.levibostian.service.DataDestroyer
import com.levibostian.service.ResetAppRunner
import com.levibostian.service.event.LogoutUserEvent
import com.levibostian.service.logger.Logger
import com.levibostian.service.manager.UserManager
import com.levibostian.service.util.InstallReferrerProcessor
import com.levibostian.view.ui.fragment.MainFragment
import kotlinx.android.synthetic.main.activity_toolbar_fragment_container.*
import javax.inject.Inject

/**
 * Extend for all root activities.
 *
 * Note: User must be logged in to use this Activity.
 */
abstract class BaseActivity: AppCompatActivity() {

    @Inject lateinit var userManager: UserManager
    @Inject lateinit var installReferrerProcessor: InstallReferrerProcessor
    @Inject lateinit var resetAppRunner: ResetAppRunner

    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateDiGraph().inject(this)
        super.onCreate(savedInstanceState)

        if (!userManager.isUserLoggedIn()) throw RuntimeException("Activity cannot run when user is not logged in")

        // I am capturing the referrer here instead of in LaunchActivity because (1) The user is logged into the app at this point and Firebase Analytics will then be associated with a user id. (2) I should only really care about where users came from that successfully logged into the app as they are users who want to use the product.
        installReferrerProcessor.process(this)
    }

    override fun onResume() {
        super.onResume()

        // Users can remove accounts in the settings app on the device. Check if they did while the app was in the background.
        if (!userManager.isUserLoggedIn()) {
            resetAppRunner.deleteAllAndReset()
        }
    }



}