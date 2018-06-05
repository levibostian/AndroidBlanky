package com.levibostian.androidblanky.service.module

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import com.levibostian.androidblanky.BuildConfig
import com.levibostian.androidblanky.module.ServiceModule
import com.levibostian.androidblanky.view.ui.MainApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import com.levibostian.androidblanky.service.model.AppConstants
import com.levibostian.androidblanky.service.manager.UserCredsManager
import com.levibostian.androidblanky.service.AppendHeadersInterceptor
import com.levibostian.androidblanky.service.DefaultErrorHandlerInterceptor
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.ResponseProcessor
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.db.manager.DatabaseManager
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import io.reactivex.schedulers.Schedulers
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

@Module class AppServiceModule(val application: MainApplication): ServiceModule {

    @Provides @Singleton override fun provideRetrofit(credsManager: UserCredsManager, defaultErrorHandlerInterceptor: DefaultErrorHandlerInterceptor): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(defaultErrorHandlerInterceptor)
                .addNetworkInterceptor(AppendHeadersInterceptor(credsManager))
                .build()
        val moshi = Moshi.Builder()
                .add(Date::class.java, Rfc3339DateJsonAdapter())
                .build()

        return Retrofit.Builder()
                .client(client)
                .baseUrl(AppConstants.API_ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
    }

    @Provides override fun provideDefaultErrorHandlerInterceptor(connectivityManager: ConnectivityManager, eventBus: EventBus): DefaultErrorHandlerInterceptor {
        return DefaultErrorHandlerInterceptor(application, eventBus, connectivityManager)
    }

    @Provides override fun provideEventbus(): EventBus {
        return EventBus.getDefault()
    }

    @Provides override fun provideConnectivityManager(): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides override fun provideService(retrofit: Retrofit): GitHubService {
        return retrofit.create(GitHubService::class.java)
    }

    @Provides override fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides override fun provideRxSharedPreferences(sharedPreferences: SharedPreferences): RxSharedPreferencesWrapper {
        return RxSharedPreferencesWrapper(sharedPreferences)
    }

    @Provides override fun provideDatabaseManager(userManager: UserManager): DatabaseManager {
        return DatabaseManager(userManager)
    }

    @Provides override fun provideDatabase(databaseManager: DatabaseManager): Database {
        return databaseManager.dbInstance(application)
    }

    @Provides override fun provideResponseProcessor(): ResponseProcessor {
        return ResponseProcessor(application)
    }

}