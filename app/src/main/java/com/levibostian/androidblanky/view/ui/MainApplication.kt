package com.levibostian.androidblanky.view.ui

import android.app.Application
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.levibostian.androidblanky.BuildConfig
import com.levibostian.androidblanky.service.pendingtasks.PendingTasksFactory
import com.levibostian.teller.Teller
import com.levibostian.wendy.WendyConfig
import com.levibostian.wendy.service.Wendy
import io.fabric.sdk.android.Fabric
import timber.log.Timber

class MainApplication : Application() {

    val component: ApplicationComponent by lazy { getApplicationComponent() }

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

        Teller.init(this)
        Wendy.init(this, PendingTasksFactory())
        WendyConfig.debug = BuildConfig.DEBUG
    }

    protected open fun getApplicationComponent(): ApplicationComponent {
        return AppApplicationComponent.Initializer.init(this)
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
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
