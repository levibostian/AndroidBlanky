package com.app.rule

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.app.di.TestAppGraph
import com.app.view.ui.TestMainApplication
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
