package com.app.view.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.hilt.work.HiltWorkerFactory
import androidx.multidex.MultiDex
import androidx.work.Configuration
import com.app.service.DataDestroyer
import com.app.service.ResetAppRunner
import com.app.service.logger.ActivityEvent
import com.app.service.logger.Logger
import com.app.service.pendingtasks.PendingTasks
import com.app.view.ui.activity.LaunchActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.levibostian.teller.Teller
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider, ResetAppRunner {

    @Inject lateinit var logger: Logger
    @Inject lateinit var dataDestroyer: DataDestroyer
    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var pendingTasks: PendingTasks

    @SuppressLint("MissingPermission") // FirebaseAnalytics.getInstance() wants you to have WAKE_LOCK permission but it's optional. Not required.
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        // manually enable firebase messaging and analytics. We disabled them from starting automatically in the manifest so that way they don't run during tests (even though it's not a huge deal if they do).
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)

        RxJavaPlugins.setErrorHandler {
            logger.errorOccurred(it)
        }

        initDependencies()

        /**
         * Tasks to run when the app starts up, put in launch activity instead of here. We want to have the Application to initialize code, not execute it. Robolectric tests run the Application and we don't want to have the code run there.
         */
    }

    private fun initDependencies() {
        Teller.init(this)

        pendingTasks.initPendingTasks()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder().setWorkerFactory(workerFactory).build()

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
