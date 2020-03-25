package com.levibostian

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins.setInitSingleSchedulerHandler
import io.reactivex.plugins.RxJavaPlugins.setInitNewThreadSchedulerHandler
import io.reactivex.plugins.RxJavaPlugins.setInitComputationSchedulerHandler
import io.reactivex.plugins.RxJavaPlugins.setInitIoSchedulerHandler
import io.reactivex.internal.schedulers.ExecutorScheduler.ExecutorWorker
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.TimeUnit

class RxImmediateSchedulerRule : TestRule {

    private val immediate = object : Scheduler() {
        override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            // this prevents StackOverflowErrors when scheduling with a delay
            return super.scheduleDirect(run, 0, unit)
        }
        override fun createWorker(): Worker {
            return ExecutorScheduler.ExecutorWorker(Runnable::run, true)
        }
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setInitIoSchedulerHandler({ _ -> immediate })
                RxJavaPlugins.setInitComputationSchedulerHandler({ _ -> immediate })
                RxJavaPlugins.setInitNewThreadSchedulerHandler({ _ -> immediate })
                RxJavaPlugins.setInitSingleSchedulerHandler({ _ -> immediate })
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> immediate }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}