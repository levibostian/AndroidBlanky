package com.levibostian.androidblanky.service.pendingtasks

import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.wendy.service.PendingTask
import com.levibostian.wendy.service.Wendy
import com.levibostian.wendy.types.PendingTaskResult
import javax.inject.Inject

class UpdateFcmTokenPendingTask(userId: String): PendingTask(dataId = userId, // userId is the dataId to simply be the unique ID for this task for Wendy to only keep 1 at a time.
        groupId = null,
        manuallyRun = false,
        tag = UpdateFcmTokenPendingTask.TAG) {

    lateinit var userManager: UserManager

    companion object {
        const val TAG = "UpdateFcmTokenPendingTask"

        fun blank(userManager: UserManager): UpdateFcmTokenPendingTask = UpdateFcmTokenPendingTask("").apply {
            this.userManager = userManager
        }
    }

    override fun runTask(): PendingTaskResult {
        // Send up the FCM token to your server
        val fcmToken = userManager.fcmPushNotificationToken

        return PendingTaskResult.SUCCESSFUL
    }

}