package com.levibostian.androidblanky.service.module

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.view.ui.MainApplication
import org.mockito.Mockito
import android.content.Context
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import com.levibostian.androidblanky.service.model.AppConstants
import com.levibostian.androidblanky.viewmodel.UserCredsManager
import com.levibostian.androidblanky.service.AppendHeadersInterceptor
import com.levibostian.androidblanky.service.DefaultErrorHandlerInterceptor
import com.levibostian.androidblanky.service.RealmInstanceWrapper
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.mock.MockRetrofit

@Module open class MockServiceModule(val application: MainApplication) {

    @Provides fun provideDefaultErrorHandlerInterceptor(connectivityManager: ConnectivityManager, eventBus: EventBus): DefaultErrorHandlerInterceptor {
        return DefaultErrorHandlerInterceptor(eventBus, connectivityManager)
    }

    @Provides fun provideEventbus(): EventBus {
        return EventBus.getDefault()
    }

    @Provides fun provideConnectivityManager(): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides @Singleton fun provideService(): GitHubService {
        return Mockito.mock(GitHubService::class.java)
    }

    @Provides @Singleton fun provideRxSharedPreferencesWrapper(): RxSharedPreferencesWrapper {
        return Mockito.mock(RxSharedPreferencesWrapper::class.java)
    }

    @Provides @Singleton fun provideSharedPreferences(): SharedPreferences {
        return Mockito.mock(SharedPreferences::class.java)
    }

    @Provides fun provideRealmWrapper(): RealmInstanceWrapper {
        return RealmInstanceWrapper()
    }

}