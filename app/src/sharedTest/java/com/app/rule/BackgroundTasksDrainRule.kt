package com.app.rule

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.lifecycle.LiveData
import java.util.concurrent.TimeUnit

/**
 * Used when you have [LiveData] in your test that you are calling [LiveData.postValue] to.
 */
class BackgroundTasksDrainRule : CountingTaskExecutorRule() {

    fun drain() {
        drainTasks(3, TimeUnit.SECONDS)
    }
}
