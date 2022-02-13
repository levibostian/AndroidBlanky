package com.app.view.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import com.app.service.ResetAppRunner
import com.app.service.logger.Logger
import com.app.view.ui.activity.LaunchActivity
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), ResetAppRunner {

    @Inject lateinit var logger: Logger

    override fun onCreate() {
        super.onCreate()
    }

    override fun deleteAllAndReset() {
        //dataDestroyer.destroyAll {
            val intent = Intent(this, LaunchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        //}
    }
}
