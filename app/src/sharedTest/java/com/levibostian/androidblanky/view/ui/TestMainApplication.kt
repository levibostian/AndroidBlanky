package com.levibostian.androidblanky.view.ui

import com.levibostian.androidblanky.di.AndroidModule
import com.levibostian.androidblanky.di.AppGraph
import com.levibostian.androidblanky.di.DaggerTestAppGraph
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