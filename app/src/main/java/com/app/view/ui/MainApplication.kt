package com.app.view.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import com.app.di.DiGraph
import com.app.service.ResetAppRunner
import com.app.service.logger.Logger
import com.app.service.util.ContextProvider
import com.app.view.ui.activity.MainActivity

class MainApplication : Application(), ResetAppRunner, ContextProvider {

    lateinit var logger: Logger

    override fun onCreate() {
        super.onCreate()

        DiGraph.contextProvider = this
    }

    override fun deleteAllAndReset() {
        // dataDestroyer.destroyAll {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        // }
    }

    override fun getContext(): Context = this
}
