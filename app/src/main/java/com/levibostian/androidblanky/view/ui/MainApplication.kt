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
import com.levibostian.androidblanky.module.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

@OpenForTesting
class MainApplication: Application() {

    private val pendingTasksFactory: PendingTasksFactory by inject()

    override fun onCreate() {
        super.onCreate()

        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        val fabric = Fabric.Builder(this).kits(Crashlytics.Builder().core(core).build()).debuggable(true).build()
        Fabric.with(fabric)

        initLibsRequiredBeforeDependenciesInitialize()

        // Application must be injected in the onCreate() so that services are able to be injected.
        startKoin {
            if (BuildConfig.DEBUG) androidLogger()
            androidContext(this@MainApplication)
            modules(getModules())
        }

        initLibsPostDependenciesInitialized()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    protected fun getModules(): List<Module> {
        return AppModules.get()
    }

    private fun initLibsRequiredBeforeDependenciesInitialize() {
        Teller.init(this)
    }

    private fun initLibsPostDependenciesInitialized() {
        Wendy.init(this, pendingTasksFactory)
        WendyConfig.debug = BuildConfig.DEBUG
        startPeriodicRunningPendingTasks()
    }

    private fun startPeriodicRunningPendingTasks() {
        val pendingTaskWorkerBuilder = PeriodicWorkRequest.Builder(PendingTasksWorker::class.java, 30, TimeUnit.MINUTES)
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        WorkManager.getInstance().enqueue(pendingTaskWorkerBuilder.setConstraints(constraints).build())
    }

}
