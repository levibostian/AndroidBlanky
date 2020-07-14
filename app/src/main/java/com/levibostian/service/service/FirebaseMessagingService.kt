package com.levibostian.service.service

import androidx.core.os.bundleOf
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.levibostian.extensions.onCreateDiGraph
import com.levibostian.service.logger.ActivityEvent
import com.levibostian.service.logger.ActivityEventParamKey
import com.levibostian.service.logger.Logger
import com.levibostian.service.manager.UserManager
import com.levibostian.service.util.NotificationUtil
import javax.inject.Inject

class FirebaseMessagingService : FirebaseMessagingService() {

    @Inject lateinit var userManager: UserManager
    @Inject lateinit var backgroundJobRunner: BackgroundJobRunner
    @Inject lateinit var logger: Logger

    override fun onCreate() {
        onCreateDiGraph().inject(this)
        super.onCreate()
    }

    // From Firebase's quickstart: https://github.com/firebase/quickstart-android/blob/master/messaging/app/src/main/java/com/google/firebase/quickstart/fcm/MyFirebaseMessagingService.java
    // There are two types of messages data messages and notification messages. Data messages are handled
    // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
    // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
    // is in the foreground. When the app is in the background an automatically generated notification is displayed.
    // When the user taps on the notification they are returned to the app. Messages containing both notification
    // and data payloads are treated as notification messages. The Firebase console always sends notification
// messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
    override fun onMessageReceived(message: RemoteMessage) {
        logger.breadcrumb(
            this, "received push notification",
            bundleOf(
                Pair("raw", message.toString())
            )
        )

        if (message.data.isNotEmpty()) {
            logger.appEventOccurred(
                ActivityEvent.PushNotificationReceived,
                mapOf(
                    Pair(ActivityEventParamKey.Type, "data")
                )
            )

            NotificationUtil.parseDataNotification(message.data)?.let { dataNotification ->
                backgroundJobRunner.handleDataPushNotification(dataNotification)
            }
        } else {
            logger.appEventOccurred(
                ActivityEvent.PushNotificationReceived,
                mapOf(
                    Pair(ActivityEventParamKey.Type, "ui")
                )
            )
        }
        /**
         * Notifications with a message payload are automatically put in the system tray as a notification for the user to click when the app is in the background. This app does not need to handle message notifications when the app is in the foreground.
         */
//        else {
//            message.notification?.let { notificationMessage ->
//                val notification = NotificationCompat.Builder(applicationContext, NotificationChannelManager.announcements.id)
//                        .setSmallIcon(R.drawable.ic_announcement_white_24dp)
//                        .setContentTitle(NotificationChannelManager.announcements.name)
//                        .setContentText(notificationMessage.body)
//                        .build()
//
//                val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//                notificationManager.notify(NotificationChannelManager.ANNOUNCEMENTS_NOTIFY_ID, notification)
//            }
//        }
    }

    override fun onNewToken(token: String) {
        logger.breadcrumb(
            this, "Push notification token received",
            bundleOf(
                Pair("token", token)
            )
        )
    }
}
