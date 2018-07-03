package com.levibostian.androidblanky.view.ui

import com.levibostian.androidblanky.service.db.module.AppDatabaseModule
import com.levibostian.androidblanky.service.module.AppManagerModule
import com.levibostian.androidblanky.service.module.AppRepositoryModule
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import com.levibostian.androidblanky.service.module.AppServiceModule
import com.levibostian.androidblanky.viewmodel.module.AppViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppServiceModule::class,
    AppManagerModule::class,
    AppRepositoryModule::class,
    AppViewModelModule::class,
    AppDatabaseModule::class])
interface AppApplicationComponent: ApplicationComponent {
    override fun inject(mainFragment: MainFragment)

    object Initializer {
        fun init(application: MainApplication): ApplicationComponent {
            return DaggerAppApplicationComponent.builder()
                    .appServiceModule(AppServiceModule(application))
                    .appDatabaseModule(AppDatabaseModule(application))
                    .appManagerModule(AppManagerModule(application))
                    .appRepositoryModule(AppRepositoryModule(application))
                    .appViewModelModule(AppViewModelModule(application))
                    .build()
        }
    }
}