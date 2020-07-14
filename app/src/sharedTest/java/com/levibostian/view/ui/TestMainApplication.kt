package com.levibostian.view.ui

import com.levibostian.di.AndroidModule
import com.levibostian.di.AppGraph
import com.levibostian.di.DaggerTestAppGraph

class TestMainApplication : MainApplication() {

    override fun initAppComponent(): AppGraph {
        return DaggerTestAppGraph
            .builder()
            .androidModule(AndroidModule(this))
            .build()
    }
}
