package com.app.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.test.platform.app.InstrumentationRegistry
import com.app.service.util.ConnectivityUtil
import com.app.service.ResetAppRunner
import com.app.service.http.PokemonHttpClient
import com.app.testutils.MockWebServer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import javax.inject.Singleton

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
