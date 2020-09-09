package com.app.service

import android.content.Context
import androidx.core.os.bundleOf
import com.app.service.logger.ActivityEvent
import com.app.service.logger.ActivityEventParamKey
import com.app.service.logger.Logger
import com.app.service.util.NotificationUtil
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent

class PushNotificationProcessor(private val context: Context) {

    private val backgroundJobRunner: BackgroundJobRunner
        get() = EntryPointAccessors.fromApplication(context, PushNotificationProcessorEntryPoint::class.java).backgroundJobRunner()
    private val logger: Logger
        get() = EntryPointAccessors.fromApplication(context, PushNotificationProcessorEntryPoint::class.java).logger()

    // From Firebase's quickstart: https://github.com/firebase/quickstart-android/blob/master/messaging/app/src/main/java/com/google/firebase/quickstart/fcm/MyFirebaseMessagingService.java
    // There are two types of messages data messages and notification messages. Data messages are handled
    // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
    // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
    // is in the foreground. When the app is in the background an automatically generated notification is displayed.
    // When the user taps on the notification they are returned to the app. Messages containing both notification
    // and data payloads are treated as notification messages. The Firebase console always sends notification
// messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
    fun processFirebaseMessage(message: RemoteMessage) {
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

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface PushNotificationProcessorEntryPoint {
        fun backgroundJobRunner(): BackgroundJobRunner
        fun logger(): Logger
    }
}
