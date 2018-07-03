package com.levibostian.androidblanky.service.pendingtasks

import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.wendy.service.PendingTask
import com.levibostian.wendy.service.PendingTasksFactory
import javax.inject.Inject

class PendingTasksFactory @Inject constructor(private val userManager: UserManager): PendingTasksFactory {

    override fun getTask(tag: String): PendingTask? {
        return when (tag) {
            UpdateFcmTokenPendingTask.TAG -> { UpdateFcmTokenPendingTask.blank(userManager) }
            else -> null
        }
    }

}
