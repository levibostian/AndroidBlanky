package com.app.view.ui

import com.app.di.AndroidModule
import com.app.di.AppGraph
import com.app.di.DaggerTestAppGraph

class TestMainApplication : MainApplication() {

    override fun initAppComponent(): AppGraph {
        return DaggerTestAppGraph
            .builder()
            .androidModule(AndroidModule(this))
            .build()
    }
}
