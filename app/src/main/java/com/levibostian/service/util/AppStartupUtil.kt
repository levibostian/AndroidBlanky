package com.levibostian.service.util

import androidx.core.os.bundleOf
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.levibostian.boquila.RemoteConfigAdapter
import com.levibostian.service.logger.ActivityEvent
import com.levibostian.service.logger.ActivityEventParamKey
import com.levibostian.service.logger.Logger
import com.levibostian.service.pendingtasks.PendingTasks
import com.levibostian.service.type.FcmTopicKey
import javax.inject.Inject

/**
 * Tasks to run when app starts goes here.
 *
 * Don't put in Application. Exceptions come up during robolectric tests there. Put in launch Activity instead.
 */
class AppStartupUtil @Inject constructor(private val pendingTasks: PendingTasks, private val remoteConfig: RemoteConfigAdapter, private val logger: Logger) {

    /**
     * Run this function before the UI of your app is shown. Some tasks effect the UI.
     */
    fun run() {
        // Activate previously fetched remote config, then kick off new refresh. Important to run in this order!
        // activate() effects the UI if it's shown. Important to only run this if a UI is not being shown.
        remoteConfig.activate()
        remoteConfig.refresh {}

        // Run all tasks that have not run successfully yet. When app start is a good chance to kick this off as it's guaranteed to run them all (the user has not killed the app from background memory) and it does not run too often to suck bandwidth from the user.
        pendingTasks.runAllTasks()

        // start subscribing to topics.
        FirebaseMessaging.getInstance().subscribeToTopic(FcmTopicKey.ProductUpdated.fcmName)
                .addOnCompleteListener { task ->
                    task.exception?.let {
                        logger.errorOccurred(it)
                    }

                    logger.appEventOccurred(ActivityEvent.PushNotificationTopicSubscribed, mapOf(
                            Pair(ActivityEventParamKey.Name, FcmTopicKey.ProductUpdated.fcmName)
                    ))
                }

        // When you want to test a push notification on a device during dev, you need the current token. This is an easy way to log it. Also includes that registering for push notifications works. Good for logging.
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { logger.errorOccurred(it) }
                        return@OnCompleteListener
                    }

                    task.result?.token?.let { token ->
                        logger.breadcrumb(this, "Existing FCM token received", bundleOf(
                                Pair("token", token)
                        ))
                    }
                })
    }

}