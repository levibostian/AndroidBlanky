package com.levibostian.service.work

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wrapper for AndroidX WorkManager. To abstract it, make it testable, and to have more control over how it's initialized.
 */
@Singleton
class WorkManagerWrapper @Inject constructor(val context: Context) {

    /**
     * Schedule workmanager to start running the app background refresh.
     */
    fun startPeriodicTasks() {
        val pendingTaskWorkerBuilder = PeriodicWorkRequest.Builder(PeriodicTasksWorker::class.java, 30, TimeUnit.MINUTES)
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        // Name the periodic job so that when we schedule it each time the app opens, we only ever have 1 periodic job made to prevent the job running X number of times.
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("Periodic background jobs", ExistingPeriodicWorkPolicy.REPLACE, pendingTaskWorkerBuilder.setConstraints(constraints).build())
    }

}