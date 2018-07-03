package com.levibostian.androidblanky.view.ui

import android.app.Application
import com.levibostian.androidblanky.module.ActivityModule
import com.levibostian.androidblanky.module.FragmentModule
import com.levibostian.androidblanky.module.ServicesModule
import com.levibostian.androidblanky.service.db.module.AppDatabaseModule
import com.levibostian.androidblanky.service.module.AppManagerModule
import com.levibostian.androidblanky.service.module.AppRepositoryModule
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import com.levibostian.androidblanky.service.module.AppServiceModule
import com.levibostian.androidblanky.viewmodel.module.AppViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityModule::class,
    FragmentModule::class,
    ServicesModule::class,
    AppServiceModule::class,
    AppManagerModule::class,
    AppRepositoryModule::class,
    AppViewModelModule::class,
    AppDatabaseModule::class])
interface AppApplicationComponent: ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: Application): Builder
        fun build(): AppApplicationComponent
    }

    object Initializer {
        fun init(application: MainApplication): ApplicationComponent {
            return DaggerAppApplicationComponent.builder()
                    .application(application)
                    .build()
        }
    }
}