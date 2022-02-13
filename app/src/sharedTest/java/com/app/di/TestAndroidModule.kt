package com.app.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.test.platform.app.InstrumentationRegistry
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import org.mockito.Mockito

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AndroidModule::class]
)
object TestAndroidModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return InstrumentationRegistry.getInstrumentation().context.getSharedPreferences("app", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideConnectivityManager(): ConnectivityManager {
        return Mockito.mock(ConnectivityManager::class.java)
    }
}
