package com.levibostian.androidblanky

import com.levibostian.androidblanky.service.module.DataSourceModule
import com.levibostian.androidblanky.service.module.MockServiceModule
import com.levibostian.androidblanky.service.module.RepositoryModule
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import com.levibostian.androidblanky.service.module.ServiceModule
import com.levibostian.androidblanky.view.ui.ApplicationComponent
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.androidblanky.viewmodel.module.ManagerModule
import com.levibostian.androidblanky.viewmodel.module.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        MockServiceModule::class,
        ManagerModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        DataSourceModule::class
))
interface MockApplicationComponent: ApplicationComponent {
    override fun inject(mainFragment: MainFragment)
    fun inject(dummyTest: DummyTest)

    object Initializer {
        fun init(application: MainApplication): MockApplicationComponent {
            return DaggerMockApplicationComponent.builder()
                    .mockServiceModule(MockServiceModule(application))
                    .managerModule(ManagerModule(application))
                    .repositoryModule(RepositoryModule(application))
                    .viewModelModule(ViewModelModule(application))
                    .dataSourceModule(DataSourceModule(application))
                    .build()
        }
    }

}