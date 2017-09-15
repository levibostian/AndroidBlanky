package com.levibostian.androidblanky.service.module

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.preference.PreferenceManager
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
import com.levibostian.androidblanky.service.db.manager.RealmInstanceManager
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.wrapper.LooperWrapper
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module open class AppServiceModule(val application: MainApplication): ServiceModule {

    @Provides @Singleton override fun provideRetrofit(credsManager: UserCredsManager, defaultErrorHandlerInterceptor: DefaultErrorHandlerInterceptor): Retrofit {
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

    @Provides override fun provideDefaultErrorHandlerInterceptor(connectivityManager: ConnectivityManager, eventBus: EventBus): DefaultErrorHandlerInterceptor {
        return DefaultErrorHandlerInterceptor(eventBus, connectivityManager)
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

    @Provides override fun provideRealmWrapper(userManager: UserManager): RealmInstanceManager {
        return RealmInstanceManager(userManager)
    }

    @Provides override fun provideLooperWrapper(): LooperWrapper {
        return LooperWrapper()
    }

}