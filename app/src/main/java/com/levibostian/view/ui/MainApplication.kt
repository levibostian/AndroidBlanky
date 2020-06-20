package com.levibostian.view.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.multidex.MultiDex
import androidx.work.*
import com.levibostian.BuildConfig
import com.levibostian.di.AndroidModule
import com.levibostian.di.AppGraph
import com.levibostian.di.DaggerAppGraph
import com.levibostian.service.ResetAppRunner
import com.levibostian.service.pendingtasks.PendingTasksFactory
import com.levibostian.service.work.PendingTasksWorker
import com.levibostian.teller.Teller
import com.levibostian.testing.OpenForTesting
import com.levibostian.view.ui.activity.LaunchActivity
import com.levibostian.wendy.WendyConfig
import com.levibostian.wendy.service.Wendy
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@OpenForTesting
class MainApplication: Application(), Configuration.Provider, ResetAppRunner {

    lateinit var appComponent: AppGraph

    @Inject lateinit var pendingTasksFactory: PendingTasksFactory

    override fun onCreate() {
        super.onCreate()

        appComponent = initAppComponent()
        appComponent.inject(this)

        initDependencies()
    }

    fun initAppComponent(): AppGraph {
        return DaggerAppGraph
                .builder()
                .androidModule(AndroidModule(this))
                .build()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initDependencies() {
        Teller.init(this)

        Wendy.init(this, pendingTasksFactory)
        WendyConfig.debug = BuildConfig.DEBUG
        startPeriodicRunningPendingTasks()
    }

    private fun startPeriodicRunningPendingTasks() {
        val pendingTaskWorkerBuilder = PeriodicWorkRequest.Builder(PendingTasksWorker::class.java, 30, TimeUnit.MINUTES)
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        WorkManager.getInstance(this).enqueue(pendingTaskWorkerBuilder.setConstraints(constraints).build())
    }

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder().build()

    override fun deleteAllAndReset() {
        // delete all data here.

        val intent = Intent(this, LaunchActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}