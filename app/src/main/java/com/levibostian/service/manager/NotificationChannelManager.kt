package com.levibostian.service.manager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import javax.inject.Inject

class NotificationChannelManager @Inject constructor(private val context: Context) {

    companion object {

        /**
         * Note: If you change the ID below, make sure to also edit the manifest's `default_notification_channel_id` metadata entry to match.
         */
        @SuppressLint("InlinedApi")
        val announcements: ChannelInfo = ChannelInfo("announcements", "Announcements", "Announcements from the team about updates to the app.", NotificationManager.IMPORTANCE_DEFAULT)

        private val allChannels = listOf(announcements)

        const val ANNOUNCEMENTS_NOTIFY_ID: Int = 1
    }

    fun createChannels() {
        if (Build.VERSION.SDK_INT < 26) return

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        allChannels.forEach { channel -> channel.create(notificationManager) }
    }

    data class ChannelInfo(val id: String, val name: String, val description: String, val importance: Int) {

        fun create(notificationManager: NotificationManager) {
            if (Build.VERSION.SDK_INT < 26) {
                throw RuntimeException("I don't think you meant to call this...")
            } else {
                val channel = createChannel()
                notificationManager.createNotificationChannel(channel)
            }
        }

        private fun createChannel(): NotificationChannel {
            if (Build.VERSION.SDK_INT < 26) {
                throw RuntimeException("I don't think you meant to call this...")
            } else {
                val channel = NotificationChannel(id, name, importance)
                channel.description = description
                channel.enableLights(false)
                channel.enableVibration(false)

                return channel
            }
        }
    }
}
