package com.app.di

import androidx.lifecycle.ViewModelProvider
import com.app.viewmodel.TestViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestViewModelModule {

    @Provides
    @Singleton
    fun provideTestViewModelFactory(): TestViewModelFactory {
        return TestViewModelFactory()
    }

    @Provides
    fun provideViewModelFactory(testViewModelFactory: TestViewModelFactory): ViewModelProvider.Factory {
        return testViewModelFactory
    }
}
