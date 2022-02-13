package com.app.di

import com.app.service.DispatcherProvider
import com.app.service.ResetAppRunner
import com.app.service.http.PokemonHttpClient
import com.app.testutils.MockWebServer
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import okhttp3.OkHttpClient
import org.mockito.Mockito

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object TestNetworkModule {

    @Provides
    @Singleton
    fun providePokemonHttpClient(client: OkHttpClient): PokemonHttpClient = PokemonHttpClient(MockWebServer.url, client)

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = Mockito.mock(DispatcherProvider::class.java)

    @Provides
    @Singleton
    fun provideAppResetRunner(): ResetAppRunner = Mockito.mock(ResetAppRunner::class.java)
}
