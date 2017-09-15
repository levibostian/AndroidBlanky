package com.levibostian.androidblanky.view.ui

import com.levibostian.androidblanky.view.ui.fragment.MainFragment

interface ApplicationComponent {
    fun inject(mainFragment: MainFragment)
}