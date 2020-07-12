package com.levibostian.view.ui.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.levibostian.extensions.onCreateDiGraph
import com.levibostian.service.ResetAppRunner
import com.levibostian.service.manager.UserManager
import com.levibostian.service.util.InstallReferrerProcessor
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

        // I am capturing the referrer here instead of in LaunchActivity because (1) The user is logged into the app at this point and Firebase Analytics will then be associated with a user id. (2) I should only really care about where users came from that successfully logged into the app as they are users who want to use the product.
        installReferrerProcessor.process(this)
    }

}