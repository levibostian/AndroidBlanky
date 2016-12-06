package com.levibostian.androidblanky

import com.levibostian.androidblanky.fragment.MainFragment
import com.levibostian.androidblanky.module.ApiModule
import com.levibostian.androidblanky.module.ManagerModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApiModule::class, ManagerModule::class))
interface ApplicationComponent {
    fun inject(mainFragment: MainFragment)
}