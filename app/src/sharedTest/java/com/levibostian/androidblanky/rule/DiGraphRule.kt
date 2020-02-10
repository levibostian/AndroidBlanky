package com.levibostian.androidblanky.rule

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.levibostian.androidblanky.di.TestAppGraph
import com.levibostian.androidblanky.view.ui.TestMainApplication
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class DiGraphRule : TestWatcher() {

    lateinit var graph: TestAppGraph
        private set

    override fun starting(description: Description?) {
        super.starting(description)

        val app = ApplicationProvider.getApplicationContext<Context>() as TestMainApplication
        graph = app.appComponent as TestAppGraph
    }

}