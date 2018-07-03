package com.levibostian.androidblanky

import android.app.Application
import com.levibostian.androidblanky.module.ActivityModule
import com.levibostian.androidblanky.module.FragmentModule
import com.levibostian.androidblanky.module.ServicesModule
import com.levibostian.androidblanky.service.db.module.AppDatabaseModule
import com.levibostian.androidblanky.service.module.*
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import com.levibostian.androidblanky.view.fragment.MainFragmentTest
import com.levibostian.androidblanky.view.ui.ApplicationComponent
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.androidblanky.service.repository.ReposRepositoryTest
import com.levibostian.androidblanky.viewmodel.module.AppViewModelModule
import com.levibostian.androidblanky.viewmodel.module.MockViewModelModule
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
    MockServiceModule::class,
    AppManagerModule::class,
    AppRepositoryModule::class,
    MockViewModelModule::class,
    MockDatabaseModule::class])
interface MockApplicationComponent: ApplicationComponent {
    fun inject(mainFragmentTest: MainFragmentTest)
    fun inject(reposRepositoryTest: ReposRepositoryTest)

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: Application): Builder
        fun build(): MockApplicationComponent
    }

    object Initializer {
        fun init(application: MainApplication): MockApplicationComponent {
            return DaggerMockApplicationComponent.builder()
                    .application(application)
                    .build()
        }
    }

}