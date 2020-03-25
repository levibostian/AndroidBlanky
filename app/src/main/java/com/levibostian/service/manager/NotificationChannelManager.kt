package com.levibostian.service.manager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Color
import android.os.Build
import javax.inject.Inject

class NotificationChannelManager @Inject constructor(private val context: Context) {

    companion object {
        @SuppressLint("InlinedApi")
        // Note: If you change the ID below, make sure to also edit the manifest's `default_notification_channel_id` metadata entry to match.
        val announcements: ChannelInfo = ChannelInfo("announcements", "Announcements", "Announcements from the team about updates to the app.", NotificationManager.IMPORTANCE_DEFAULT)
        @SuppressLint("InlinedApi")
        val errors: ChannelInfo = ChannelInfo("errors", "Errors", "When errors occur in the app that need your attention.", NotificationManager.IMPORTANCE_HIGH)

        private val allChannels = listOf(announcements, errors)

        const val ANNOUNCEMENTS_NOTIFY_ID: Int = 1
        const val UPDATE_APP_ID: Int = 2
    }

    fun createChannels() {
        if (Build.VERSION.SDK_INT < 26) { return }

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