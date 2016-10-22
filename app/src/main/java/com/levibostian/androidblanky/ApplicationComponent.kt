package com.levibostian.androidblanky

import com.levibostian.androidblanky.activity.MainActivity
import com.levibostian.androidblanky.fragment.MainFragment
import com.levibostian.androidblanky.module.ApiModule
import com.levibostian.androidblanky.module.DaoModule
import com.levibostian.androidblanky.module.ManagerModule
import com.levibostian.androidblanky.module.UtilModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApiModule::class, DaoModule::class, ManagerModule::class, UtilModule::class))
interface ApplicationComponent {
    fun inject(mainFragment: MainFragment)
}