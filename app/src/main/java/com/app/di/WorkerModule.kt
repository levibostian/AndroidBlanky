package com.app.di

import com.app.service.work.PeriodicTasksWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(PeriodicTasksWorker::class)
    internal abstract fun bindPendingTasksWorker(worker: PeriodicTasksWorker.Factory): ChildWorkerFactory
}
