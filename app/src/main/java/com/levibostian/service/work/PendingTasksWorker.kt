package com.levibostian.service.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.levibostian.wendy.service.Wendy

class PendingTasksWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    override fun doWork(): Result {
        Wendy.shared.runTasks(null)

        return Result.success()
    }

}