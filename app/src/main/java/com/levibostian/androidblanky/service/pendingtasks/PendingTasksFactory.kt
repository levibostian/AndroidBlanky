package com.levibostian.androidblanky.service.pendingtasks

import com.levibostian.wendy.service.PendingTask
import com.levibostian.wendy.service.PendingTasksFactory

class PendingTasksFactory: PendingTasksFactory {

    override fun getTask(tag: String): PendingTask? {
        return when (tag) {
            else -> null
        }
    }

}
