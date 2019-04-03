package com.levibostian.androidblanky.service.module

import android.accounts.AccountManager
import android.app.Application
import android.content.SharedPreferences
import com.levibostian.androidblanky.service.GitHubService
import android.content.Context
import android.net.ConnectivityManager
import com.levibostian.androidblanky.module.ServiceModule
import com.levibostian.androidblanky.service.DataDestroyer
import com.levibostian.androidblanky.service.db.Database
import javax.inject.Singleton
import com.levibostian.androidblanky.service.interceptor.DefaultErrorHandlerInterceptor
import com.levibostian.androidblanky.service.logger.Logger
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.util.ResponseProcessor
import com.levibostian.androidblanky.AppConstants
import com.nhaarman.mockitokotlin2.mock
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit

@Module class MockServiceModule: ServiceModule {

    @Provides override fun provideRetrofit(userManager: UserManager, defaultErrorHandlerInterceptor: DefaultErrorHandlerInterceptor, missingDataResponseInterceptor: MissingDataResponseInterceptor): Retrofit {
        val client = OkHttpClient.Builder()
                .build()

        return Retrofit.Builder()
                .client(client).baseUrl(AppConstants.API_ENDPOINT)
                .build()
    }

}