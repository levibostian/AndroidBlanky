package com.app.di

import android.content.Context
import com.app.service.logger.Logger
import com.app.service.util.ConnectivityUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object DependencyModule {

    @Provides
    fun provideOkHttp(@ApplicationContext context: Context, connectivityUtil: ConnectivityUtil, logger: Logger): OkHttpClient = OkHttpClient.Builder()
        .build()

}
