package com.app.service.work

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.app.service.BackgroundJobRunner

class PeriodicTasksWorker @WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val backgroundJobRunner: BackgroundJobRunner
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        backgroundJobRunner.runPeriodicJobs()

        return Result.success()
    }
}
