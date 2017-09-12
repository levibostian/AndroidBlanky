package com.levibostian.androidblanky.view.ui

import com.levibostian.androidblanky.MockApplicationComponent

class TestMainApplication : MainApplication() {

    override fun getApplicationComponent(): ApplicationComponent {
        return MockApplicationComponent.Initializer.init(this)
    }

}