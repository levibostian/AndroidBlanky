package com.levibostian.view.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.multidex.MultiDex
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.levibostian.BuildConfig
import com.levibostian.di.AndroidModule
import com.levibostian.di.AppGraph
import com.levibostian.service.DataDestroyer
import com.levibostian.service.ResetAppRunner
import com.levibostian.service.logger.ActivityEvent
import com.levibostian.service.logger.Logger
import com.levibostian.service.pendingtasks.PendingTasksFactory
import com.levibostian.teller.Teller
import com.levibostian.testing.OpenForTesting
import com.levibostian.view.ui.activity.LaunchActivity
import com.levibostian.wendy.WendyConfig
import com.levibostian.wendy.service.Wendy
import io.reactivex.plugins.RxJavaPlugins
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OpenForTesting
class MainApplication: Application(), Configuration.Provider, ResetAppRunner {

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

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initDependencies() {
        Teller.init(this)

        Wendy.init(this, pendingTasksFactory)
        WendyConfig.debug = BuildConfig.DEBUG
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