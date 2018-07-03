package com.levibostian.androidblanky.service.module

import android.accounts.AccountManager
import android.app.Application
import android.content.SharedPreferences
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.view.ui.MainApplication
import org.mockito.Mockito
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.androidblanky.module.ServiceModule
import com.levibostian.androidblanky.service.DataDestroyer
import com.levibostian.androidblanky.service.analytics.AppAnalytics
import com.levibostian.androidblanky.service.dao.ReposDao
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.db.manager.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.levibostian.androidblanky.service.interceptor.DefaultErrorHandlerInterceptor
import com.levibostian.androidblanky.service.interceptor.MissingDataResponseInterceptor
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.util.ResponseProcessor
import com.levibostian.androidblanky.service.model.AppConstants
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
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

    @Provides override fun provideDefaultErrorHandlerInterceptor(application: Application, connectivityManager: ConnectivityManager, eventBus: EventBus): DefaultErrorHandlerInterceptor {
        return DefaultErrorHandlerInterceptor(application, eventBus, connectivityManager)
    }

    @Provides override fun provideEventbus(): EventBus = EventBus.getDefault()

    @Provides override fun provideConnectivityManager(application: Application): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides override fun provideMissingDataResponseInterceptor(application: Application): MissingDataResponseInterceptor {
        return MissingDataResponseInterceptor(application)
    }

    @Provides @Singleton override fun provideDataDestroyer(db: Database, accountManager: AccountManager, userManager: UserManager, sharedPreferences: SharedPreferences): DataDestroyer = Mockito.mock(DataDestroyer::class.java)

    @Provides @Singleton override fun provideService(retrofit: Retrofit): GitHubService = Mockito.mock(GitHubService::class.java)

    @Provides @Singleton override fun provideRxSharedPreferences(sharedPreferences: SharedPreferences): RxSharedPreferencesWrapper = Mockito.mock(RxSharedPreferencesWrapper::class.java)

    @Provides @Singleton override fun provideSharedPreferences(application: Application): SharedPreferences = Mockito.mock(SharedPreferences::class.java)

    @Provides @Singleton override fun provideResponseProcessor(application: Application): ResponseProcessor = Mockito.mock(ResponseProcessor::class.java)

    @Provides @Singleton override fun provideAppAnalytics(application: Application): AppAnalytics = Mockito.mock(AppAnalytics::class.java)

}