package com.levibostian.androidblanky.service.service

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.extensions.onCreateDiGraph
import com.levibostian.androidblanky.service.manager.NotificationChannelManager
import com.levibostian.androidblanky.service.manager.UserManager
import javax.inject.Inject

class FirebaseMessagingService: FirebaseMessagingService() {

    @Inject lateinit var userManager: UserManager

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
        if (message.data.isNotEmpty()) {
            // Job to run.
        } else {
            message.notification?.let { notificationMessage ->
                val notification = NotificationCompat.Builder(applicationContext, NotificationChannelManager.announcements.id)
                        .setSmallIcon(R.drawable.ic_announcement_white_24dp)
                        .setContentTitle(NotificationChannelManager.announcements.name)
                        .setContentText(notificationMessage.body)
                        .build()

                val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                notificationManager.notify(NotificationChannelManager.ANNOUNCEMENTS_NOTIFY_ID, notification)
            }
        }
    }

    override fun onNewToken(token: String) {
        userManager.fcmPushNotificationToken = token
    }

}