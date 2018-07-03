package com.levibostian.androidblanky

import com.levibostian.androidblanky.service.module.MockServiceModule
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import com.levibostian.androidblanky.view.fragment.MainFragmentTest
import com.levibostian.androidblanky.view.ui.ApplicationComponent
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.androidblanky.service.module.AppManagerModule
import com.levibostian.androidblanky.service.module.AppRepositoryModule
import com.levibostian.androidblanky.service.module.MockDatabaseModule
import com.levibostian.androidblanky.service.repository.ReposRepositoryTest
import com.levibostian.androidblanky.viewmodel.module.AppViewModelModule
import com.levibostian.androidblanky.viewmodel.module.MockViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MockServiceModule::class,
    AppManagerModule::class,
    AppRepositoryModule::class,
    MockViewModelModule::class,
    MockDatabaseModule::class])
interface MockApplicationComponent: ApplicationComponent {
    override fun inject(mainFragment: MainFragment)
    fun inject(mainFragmentTest: MainFragmentTest)
    fun inject(reposRepositoryTest: ReposRepositoryTest)

    object Initializer {
        fun init(application: MainApplication): MockApplicationComponent {
            return DaggerMockApplicationComponent.builder()
                    .mockServiceModule(MockServiceModule(application))
                    .appManagerModule(AppManagerModule(application))
                    .appRepositoryModule(AppRepositoryModule(application))
                    .mockDatabaseModule(MockDatabaseModule(application))
                    .mockViewModelModule(MockViewModelModule(application))
                    .build()
        }
    }

}