package com.levibostian.androidblanky

import com.levibostian.androidblanky.service.module.MockServiceModule
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import com.levibostian.androidblanky.view.fragment.MainFragmentTest
import com.levibostian.androidblanky.view.ui.ApplicationComponent
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.androidblanky.service.module.AppManagerModule
import com.levibostian.androidblanky.service.module.AppRepositoryModule
import com.levibostian.androidblanky.viewmodel.module.AppViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        MockServiceModule::class,
        AppManagerModule::class,
        AppRepositoryModule::class,
        AppViewModelModule::class
))
interface MockApplicationComponent: ApplicationComponent {
    override fun inject(mainFragment: MainFragment)
    fun inject(mainFragmentTest: MainFragmentTest)

    object Initializer {
        fun init(application: MainApplication): MockApplicationComponent {
            return DaggerMockApplicationComponent.builder()
                    .mockServiceModule(MockServiceModule(application))
                    .appManagerModule(AppManagerModule(application))
                    .appRepositoryModule(AppRepositoryModule(application))
                    .appViewModelModule(AppViewModelModule(application))
                    .build()
        }
    }

}