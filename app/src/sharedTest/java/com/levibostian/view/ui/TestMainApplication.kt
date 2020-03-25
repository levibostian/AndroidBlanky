package com.levibostian.view.ui

import com.levibostian.di.AndroidModule
import com.levibostian.di.AppGraph
import com.levibostian.di.DaggerTestAppGraph
import com.levibostian.swapper.SwapperView

class TestMainApplication: MainApplication() {

    override fun onCreate() {
        super.onCreate()

        SwapperView.config.animationDuration = 0
    }

    override fun initAppComponent(): AppGraph {
        return DaggerTestAppGraph
                .builder()
                .androidModule(AndroidModule(this))
                .build()
    }

}