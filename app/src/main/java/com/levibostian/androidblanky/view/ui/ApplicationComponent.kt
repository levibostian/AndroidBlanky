package com.levibostian.androidblanky.view.ui

import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import com.levibostian.androidblanky.service.module.ApiModule
import com.levibostian.androidblanky.viewmodel.module.ManagerModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApiModule::class, ManagerModule::class))
interface ApplicationComponent {
    fun inject(mainFragment: MainFragment)
}