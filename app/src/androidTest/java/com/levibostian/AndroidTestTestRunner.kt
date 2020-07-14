package com.levibostian

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import androidx.test.runner.AndroidJUnitRunner
import com.levibostian.view.ui.TestMainApplication
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy

class AndroidTestTestRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle?) {
        // Required only for testing to disable the *networking on UI thread* rule. This is because of our use of the OkHttp mock web server. Because we use the mock web server and Dagger to create the graph, the execution goes as follows: (1) Dagger graph made (2) app's MainApplication injected by graph (3) some things such as wendy pending tasks factory initialized which require some dependencies such as repositories (4) these repositories require Retrofit interface instances and when testing, these Retrofit instances require the mockwebserver's URL to create Retrofit instances. When the url is requested in the mock web server, it starts up the server. Well, when the server is started up, it can't be done on the UI thread.
        // So, we will disable this rule only on the testing because this problem is not easily fixable since we don't control very well when the DI graph is made and injected especially when injection should be able to be injected on any thread it wishes!
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        super.onCreate(arguments)
    }

    override fun onStart() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())

        val app = targetContext.applicationContext
        cleanupStatusBar(app)

        super.onStart()
    }

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestMainApplication::class.java.name, context)
    }

    private fun cleanupStatusBar(context: Context) {
        val extras = Bundle()
        extras.putBoolean("enabled", true)
        extras.putString("background_color", context.getColor(R.color.primary_darker).toString())
        extras.putString("background_color_name", context.getString(R.string.app_name))
        val intent = Intent("com.emmaguy.cleanstatusbar.TOGGLE")
        intent.putExtras(extras)
        context.sendBroadcast(intent)
    }
}
