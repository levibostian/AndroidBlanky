package com.levibostian.androidblanky.service.module

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.preference.PreferenceManager
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
import com.levibostian.androidblanky.service.db.manager.RealmInstanceManager
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.wrapper.LooperWrapper
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module open class ServiceModule(val application: MainApplication) {

    @Provides @Singleton fun provideRetrofit(credsManager: UserCredsManager, defaultErrorHandlerInterceptor: DefaultErrorHandlerInterceptor): Retrofit {
        val client = OkHttpClient.Builder()
                .addInterceptor(defaultErrorHandlerInterceptor)
                .addNetworkInterceptor(AppendHeadersInterceptor(credsManager))
                .build()

        return Retrofit.Builder()
                .client(client).baseUrl(AppConstants.API_ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    @Provides fun provideDefaultErrorHandlerInterceptor(connectivityManager: ConnectivityManager, eventBus: EventBus): DefaultErrorHandlerInterceptor {
        return DefaultErrorHandlerInterceptor(eventBus, connectivityManager)
    }

    @Provides fun provideEventbus(): EventBus {
        return EventBus.getDefault()
    }

    @Provides fun provideConnectivityManager(): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides open fun provideService(retrofit: Retrofit): GitHubService {
        return retrofit.create(GitHubService::class.java)
    }

    @Provides open fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides open fun provideRxSharedPreferences(sharedPreferences: SharedPreferences): RxSharedPreferencesWrapper {
        return RxSharedPreferencesWrapper(sharedPreferences)
    }

    @Provides fun provideRealmWrapper(userManager: UserManager): RealmInstanceManager {
        return RealmInstanceManager(userManager)
    }

    @Provides fun provideLooperWrapper(): LooperWrapper {
        return LooperWrapper()
    }

}