package com.levibostian.androidblanky.view.ui

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.levibostian.androidblanky.service.pendingtasks.PendingTasksFactory
import com.levibostian.androidblanky.service.work.PendingTasksWorker
import com.levibostian.androidblanky.testing.OpenForTesting
import com.levibostian.teller.Teller
import com.levibostian.wendy.WendyConfig
import com.levibostian.wendy.service.Wendy
import io.fabric.sdk.android.Fabric
import java.util.concurrent.TimeUnit
import androidx.multidex.MultiDex
import com.levibostian.androidblanky.BuildConfig
import com.levibostian.androidblanky.di.*
import javax.inject.Inject

@OpenForTesting
class MainApplication: Application() {

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

}
