package com.levibostian.androidblanky.view.ui

import android.app.Activity
import android.app.Application
import android.app.Service
import android.util.Log
import androidx.fragment.app.Fragment
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.levibostian.androidblanky.BuildConfig
import com.levibostian.androidblanky.service.manager.NotificationChannelManager
import com.levibostian.androidblanky.service.pendingtasks.PendingTasksFactory
import com.levibostian.androidblanky.testing.OpenForTesting
import com.levibostian.teller.Teller
import com.levibostian.wendy.WendyConfig
import com.levibostian.wendy.service.Wendy
import dagger.android.*
import dagger.android.support.HasSupportFragmentInjector
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Inject

@OpenForTesting
class MainApplication : Application(), HasActivityInjector, HasSupportFragmentInjector, HasServiceInjector {

    val component: ApplicationComponent by lazy { getApplicationComponent() }

    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>
    @Inject lateinit var pendingTasksFactory: PendingTasksFactory

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

        component.inject(this)

        Teller.init(this)
        Wendy.init(this, pendingTasksFactory)
        WendyConfig.debug = BuildConfig.DEBUG
    }

    protected fun getApplicationComponent(): ApplicationComponent {
        return AppApplicationComponent.Initializer.init(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingFragmentInjector
    override fun serviceInjector(): AndroidInjector<Service> = dispatchingServiceInjector

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
