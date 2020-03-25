package com.levibostian.view.ui

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import androidx.fragment.app.Fragment
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.levibostian.service.pendingtasks.PendingTasksFactory
import com.levibostian.service.work.PendingTasksWorker
import com.levibostian.testing.OpenForTesting
import com.levibostian.teller.Teller
import com.levibostian.wendy.WendyConfig
import com.levibostian.wendy.service.Wendy
import io.fabric.sdk.android.Fabric
import java.util.concurrent.TimeUnit
import androidx.multidex.MultiDex
import androidx.work.*
import com.levibostian.BuildConfig
import com.levibostian.di.*
import javax.inject.Inject

@OpenForTesting
class MainApplication: Application(), Configuration.Provider {

    lateinit var appComponent: AppGraph

    @Inject lateinit var pendingTasksFactory: PendingTasksFactory

    override fun onCreate() {
        super.onCreate()

        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        val fabric = Fabric.Builder(this).kits(Crashlytics.Builder().core(core).build()).debuggable(true).build()
        Fabric.with(fabric)

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

}
