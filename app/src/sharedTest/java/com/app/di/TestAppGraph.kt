package com.app.di

import com.app.view.fragment.MainFragmentTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidModule::class,
        TestDatabaseModule::class,
        DependencyModule::class,
        TestNetworkModule::class,
        ViewModelModule::class
    ]
)
interface TestAppGraph : AppGraph {
    fun inject(mainFragmentTest: MainFragmentTest)
}
