package com.levibostian.androidblanky.service.pendingtasks

import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.wendy.service.PendingTask
import com.levibostian.wendy.service.PendingTasksFactory
import javax.inject.Inject

class PendingTasksFactory(private val application: MainApplication): PendingTasksFactory {

    @Inject lateinit var userManager: UserManager

    override fun getTask(tag: String): PendingTask? {
        application.component.inject(this)

        return when (tag) {
            UpdateFcmTokenPendingTask.TAG -> { UpdateFcmTokenPendingTask.blank(userManager) }
            else -> null
        }
    }

}
