package com.levibostian.androidblanky.view.ui

import com.levibostian.androidblanky.view.ui.activity.LaunchActivity
import com.levibostian.androidblanky.view.ui.activity.MainActivity
import com.levibostian.androidblanky.view.ui.fragment.MainFragment

interface ApplicationComponent {
    fun inject(mainFragment: MainFragment)
    fun inject(launchActivity: LaunchActivity)
    fun inject(mainActivity: MainActivity)
}