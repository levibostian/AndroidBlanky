package com.app.di

import android.app.Application
import com.app.service.DispatcherProvider
import com.app.service.ImplementationDispatcherProvider
import com.app.service.ResetAppRunner
import com.app.service.http.PokemonHttpClient
import com.app.view.ui.MainApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providePokemonHttpClient(client: OkHttpClient): PokemonHttpClient = PokemonHttpClient("https://pokeapi.co/api/v2", client)

    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = ImplementationDispatcherProvider()

    @Provides
    fun provideAppResetRunner(application: Application): ResetAppRunner = (application as MainApplication)
}
