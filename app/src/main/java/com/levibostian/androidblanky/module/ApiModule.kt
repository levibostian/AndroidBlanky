package com.levibostian.androidblanky.module

import com.levibostian.androidblanky.MainApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import com.levibostian.androidblanky.AppConstants
import com.levibostian.androidblanky.manager.UserCredsManager
import com.levibostian.androidblanky.service.AppendHeadersInterceptor
import com.levibostian.androidblanky.service.GitHubService
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.*

@Module open class ApiModule(val application: MainApplication) {

    @Provides
    @Singleton
    fun provideRetrofit(credsManager: UserCredsManager): Retrofit {
        val client = OkHttpClient.Builder()
                .addNetworkInterceptor(AppendHeadersInterceptor(credsManager))
                .build()

        return Retrofit.Builder()
                .client(client).baseUrl(AppConstants.API_ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    @Provides
    fun provideService(retrofit: Retrofit): GitHubService {
        return retrofit.create(GitHubService::class.java)
    }

}