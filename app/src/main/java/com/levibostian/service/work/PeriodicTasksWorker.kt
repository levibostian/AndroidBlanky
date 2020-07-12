package com.levibostian.service.work


import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.levibostian.di.ChildWorkerFactory
import com.levibostian.service.service.BackgroundJobRunner
import javax.inject.Inject

class PeriodicTasksWorker(appContext: Context,
                          workerParams: WorkerParameters,
                          private val backgroundJobRunner: BackgroundJobRunner): Worker(appContext, workerParams) {

    override fun doWork(): Result {
        backgroundJobRunner.runPeriodicJobs()

        return Result.success()
    }

    class Factory @Inject constructor(
            private val backgroundJobRunner: BackgroundJobRunner
    ): ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): Worker {
            return PeriodicTasksWorker(appContext, params, backgroundJobRunner)
        }
    }
}