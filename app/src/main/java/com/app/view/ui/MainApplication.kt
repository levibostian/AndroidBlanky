package com.app.view.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.multidex.MultiDex
import androidx.work.Configuration
import com.app.BuildConfig
import com.app.di.AndroidModule
import com.app.di.AppGraph
import com.app.di.DaggerAppGraph
import com.app.service.DataDestroyer
import com.app.service.ResetAppRunner
import com.app.service.logger.ActivityEvent
import com.app.service.logger.Logger
import com.app.service.pendingtasks.PendingTasksFactory
import com.app.view.ui.activity.LaunchActivity
import com.google.firebase.FirebaseApp
import com.levibostian.teller.Teller
import com.levibostian.wendy.WendyConfig
import com.levibostian.wendy.service.Wendy
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject

class MainApplication : Application(), Configuration.Provider, ResetAppRunner {

    lateinit var appComponent: AppGraph

    @Inject lateinit var pendingTasksFactory: PendingTasksFactory
    @Inject lateinit var logger: Logger
    @Inject lateinit var dataDestroyer: DataDestroyer

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        appComponent = initAppComponent()
        appComponent.inject(this)

        RxJavaPlugins.setErrorHandler {
            logger.errorOccurred(it)
        }

        initDependencies()

        /**
         * Tasks to run when the app starts up, put in launch activity instead of here. We want to have the Application to initialize code, not execute it. Robolectric tests run the Application and we don't want to have the code run there.
         */
    }

    fun initAppComponent(): AppGraph {
        return DaggerAppGraph
            .builder()
            .androidModule(AndroidModule(this))
            .build()
    }

    private fun initDependencies() {
        Teller.init(this)

        Wendy.init(this, pendingTasksFactory)
        WendyConfig.debug = BuildConfig.DEBUG
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder().build()

    override fun deleteAllAndReset() {
        logger.appEventOccurred(ActivityEvent.Logout, null)
        logger.setUserId(null)

        dataDestroyer.destroyAll {
            val intent = Intent(this, LaunchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
