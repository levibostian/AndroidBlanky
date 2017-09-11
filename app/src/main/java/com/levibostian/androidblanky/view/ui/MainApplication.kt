package com.levibostian.androidblanky.view.ui

import android.app.Application
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.levibostian.androidblanky.BuildConfig
import com.levibostian.androidblanky.service.error.fatal.FatalAppError
import com.levibostian.androidblanky.service.error.nonfatal.NonFatalAppError
import com.levibostian.androidblanky.service.module.DataSourceModule
import com.levibostian.androidblanky.service.module.RepositoryModule
import com.levibostian.androidblanky.service.module.ServiceModule
import com.levibostian.androidblanky.viewmodel.module.ManagerModule
import com.levibostian.androidblanky.viewmodel.module.ViewModelModule
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber
import java.io.IOException
import java.net.SocketException
import io.fabric.sdk.android.services.common.Crash

class MainApplication : Application() {

    companion object {
        @JvmStatic lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        val fabric = Fabric.Builder(this).kits(Crashlytics.Builder().core(core).build()).debuggable(true).build()
        Fabric.with(fabric)

        RxJavaPlugins.setErrorHandler { e ->
            var error = e
            if (e is UndeliverableException) {
                error = e.cause!!
            }

            when (error) {
                is IOException, is SocketException -> return@setErrorHandler // Network problem.
                is InterruptedException -> return@setErrorHandler // Some blocking code was interrupted by a dispose call.
                is NonFatalAppError -> return@setErrorHandler // Custom app error set as non-fatal.
                is FatalAppError -> {
                    Timber.log(Log.ERROR, error)
                    return@setErrorHandler
                }
                else -> {
                    Timber.log(Log.ERROR, error)
                    Thread.currentThread().uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), error)
                }
            }
        }

        component = DaggerApplicationComponent.builder()
                .serviceModule(ServiceModule(this))
                .managerModule(ManagerModule(this))
                .repositoryModule(RepositoryModule(this))
                .viewModelModule(ViewModelModule(this))
                .dataSourceModule(DataSourceModule(this))
                .build()

        configureRealm()
    }

    private fun configureRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .schemaVersion(0)
                .build()
        Realm.setDefaultConfiguration(config)
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

            Crashlytics.log(priority, tag, message)

            if (t != null) {
                if (priority == Log.ERROR) {
                    Crashlytics.logException(t)
                } else if (priority == Log.WARN) {
                    // don't do anything since I am already logging it above.
                }
            }
        }
    }

}