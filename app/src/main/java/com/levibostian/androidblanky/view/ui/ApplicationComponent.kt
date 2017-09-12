package com.levibostian.androidblanky.view.ui

import com.levibostian.androidblanky.service.module.DataSourceModule
import com.levibostian.androidblanky.service.module.RepositoryModule
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import com.levibostian.androidblanky.service.module.ServiceModule
import com.levibostian.androidblanky.viewmodel.module.ManagerModule
import com.levibostian.androidblanky.viewmodel.module.ViewModelModule
import dagger.Component
import javax.inject.Singleton

interface ApplicationComponent {
    fun inject(mainFragment: MainFragment)
}