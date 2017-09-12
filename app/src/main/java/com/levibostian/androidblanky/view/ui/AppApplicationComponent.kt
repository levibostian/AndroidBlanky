package com.levibostian.androidblanky.view.ui

import com.levibostian.androidblanky.service.module.DataSourceModule
import com.levibostian.androidblanky.service.module.RepositoryModule
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import com.levibostian.androidblanky.service.module.ServiceModule
import com.levibostian.androidblanky.viewmodel.module.ManagerModule
import com.levibostian.androidblanky.viewmodel.module.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ServiceModule::class,
        ManagerModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        DataSourceModule::class
))
interface AppApplicationComponent: ApplicationComponent {
    override fun inject(mainFragment: MainFragment)

    object Initializer {
        fun init(application: MainApplication): ApplicationComponent {
            return DaggerAppApplicationComponent.builder()
                    .serviceModule(ServiceModule(application))
                    .managerModule(ManagerModule(application))
                    .repositoryModule(RepositoryModule(application))
                    .viewModelModule(ViewModelModule(application))
                    .dataSourceModule(DataSourceModule(application))
                    .build()
        }
    }
}