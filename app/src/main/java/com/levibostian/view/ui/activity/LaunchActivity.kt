package com.levibostian.view.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.levibostian.extensions.onCreateDiGraph
import com.levibostian.service.logger.ActivityEvent
import com.levibostian.service.logger.Logger
import com.levibostian.service.manager.NotificationChannelManager
import com.levibostian.service.util.AppStartupUtil
import com.levibostian.service.util.DynamicLinkAction
import com.levibostian.service.util.DynamicLinksProcessor
import com.levibostian.service.work.WorkManagerWrapper
import javax.inject.Inject

class LaunchActivity : AppCompatActivity() {

    @Inject lateinit var notificationChannelManager: NotificationChannelManager
    @Inject lateinit var logger: Logger
    @Inject lateinit var workerManager: WorkManagerWrapper
    @Inject lateinit var appStartupUtil: AppStartupUtil

    private val LOGIN_ACTIVITY_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateDiGraph().inject(this)
        super.onCreate(savedInstanceState)

        notificationChannelManager.createChannels()

        // Run startup tasks before UI shows up. Some tasks may change the UI if called after the UI is shown.
        appStartupUtil.run()

        getDynamicLinkIfExists { handledNavigation ->
            if (!handledNavigation) {
                goToMainPartOfApp()
            }
        }
    }

    private fun goToMainPartOfApp() {
        startActivity(MainActivity.getIntent(this))

        workerManager.startPeriodicTasks()

        finish()
    }

    private fun getDynamicLinkIfExists(onComplete: (handled: Boolean) -> Unit) {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                var deepLink = pendingDynamicLinkData?.link

                // Just because Firebase did not find a dynamic link doesn't mean there is not one at all (dynamic links that are not short links may not be captured by Firebase). We check the intent to see if a dynamic link exists there and handle it, too.
                if (deepLink == null) {
                    intent?.dataString?.let { deepLink = Uri.parse(it) }
                }

                if (deepLink == null) return@addOnSuccessListener onComplete(false)

                onComplete(handleDeepLink(deepLink!!))
            }
            .addOnFailureListener(this) { error ->
                logger.errorOccurred(error)

                onComplete(false)
            }
    }

    /**
     * Takes a dynamic link (aka: deep link), parses it, determines if there is an action, if there is an action, performs it.
     *
     * @return If there was an action that was acted upon that launched a UI to the user. This tells the Activity if if should launch an activity if one has not been launched already.
     */
    private fun handleDeepLink(deepLink: Uri): Boolean {
        logger.appEventOccurred(ActivityEvent.OpenedDynamicLink, null)

        val action = DynamicLinksProcessor.getActionFromDynamicLink(deepLink) ?: return false

        return when (action) {
            is DynamicLinkAction.PasswordlessTokenExchange -> {
                logger.breadcrumb(this, "Passwordless token found in dynamic link", null)

                startActivityForResult(LoginActivity.getIntent(this, action.token), LOGIN_ACTIVITY_REQUEST_CODE)

                true
            }
        }
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
