package com.levibostian.androidblanky.service.module

import android.content.SharedPreferences
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.view.ui.MainApplication
import org.mockito.Mockito
import android.content.Context
import android.net.ConnectivityManager
import com.levibostian.androidblanky.module.ServiceModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.levibostian.androidblanky.service.DefaultErrorHandlerInterceptor
import com.levibostian.androidblanky.service.db.manager.RealmInstanceManager
import com.levibostian.androidblanky.service.manager.UserCredsManager
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.model.AppConstants
import com.levibostian.androidblanky.service.wrapper.LooperWrapper
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit

@Module open class MockServiceModule(val application: MainApplication): ServiceModule {

    @Provides override fun provideRetrofit(credsManager: UserCredsManager, defaultErrorHandlerInterceptor: DefaultErrorHandlerInterceptor): Retrofit {
        val client = OkHttpClient.Builder()
                .build()

        return Retrofit.Builder()
                .client(client).baseUrl(AppConstants.API_ENDPOINT)
                .build()
    }

    @Provides override fun provideDefaultErrorHandlerInterceptor(connectivityManager: ConnectivityManager, eventBus: EventBus): DefaultErrorHandlerInterceptor {
        return DefaultErrorHandlerInterceptor(eventBus, connectivityManager)
    }

    @Provides override fun provideEventbus(): EventBus = EventBus.getDefault()

    @Provides override fun provideConnectivityManager(): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides @Singleton override fun provideService(retrofit: Retrofit): GitHubService = Mockito.mock(GitHubService::class.java)

    @Provides @Singleton override fun provideRxSharedPreferences(sharedPreferences: SharedPreferences): RxSharedPreferencesWrapper = Mockito.mock(RxSharedPreferencesWrapper::class.java)

    @Provides @Singleton override fun provideSharedPreferences(): SharedPreferences = Mockito.mock(SharedPreferences::class.java)

    @Provides @Singleton override fun provideRealmWrapper(userManager: UserManager): RealmInstanceManager = Mockito.mock(RealmInstanceManager::class.java)

    @Provides @Singleton override fun provideLooperWrapper(): LooperWrapper = Mockito.mock(LooperWrapper::class.java)

}