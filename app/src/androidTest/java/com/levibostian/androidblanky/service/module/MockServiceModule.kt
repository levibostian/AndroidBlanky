package com.levibostian.androidblanky.service.module

import android.content.SharedPreferences
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.view.ui.MainApplication
import org.mockito.Mockito
import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.levibostian.androidblanky.service.DefaultErrorHandlerInterceptor
import com.levibostian.androidblanky.service.db.manager.RealmInstanceManager
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
import org.greenrobot.eventbus.EventBus

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

    @Provides @Singleton fun provideRealmWrapper(): RealmInstanceManager {
        return Mockito.mock(RealmInstanceManager::class.java)
    }

}