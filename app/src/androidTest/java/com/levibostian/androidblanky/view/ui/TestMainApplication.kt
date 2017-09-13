package com.levibostian.androidblanky.view.ui

import com.levibostian.androidblanky.MockApplicationComponent

class TestMainApplication : MainApplication() {

    companion object {
        var overrideComponent: ApplicationComponent? = null
    }

    override fun getApplicationComponent(): ApplicationComponent {
        return overrideComponent ?: MockApplicationComponent.Initializer.init(this)
    }

}