package com.levibostian.androidblanky.module;

import com.levibostian.androidblanky.MainApplication;
import com.levibostian.androidblanky.service.GitHubApi;
import dagger.Module;
import dagger.Provides;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.Retrofit;

import javax.inject.Singleton;

@Module
public class ApiModule {

    private MainApplication mApplication;

    private final String mApiBaseUrl;

    public ApiModule(MainApplication application) {
        mApplication = application;

        mApiBaseUrl = "https://api.github.com";
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                       .baseUrl(mApiBaseUrl)
                       .addConverterFactory(JacksonConverterFactory.create()).build();
    }

    @Provides
    public GitHubApi provideApi(Retrofit retrofit) {
        return retrofit.create(GitHubApi.class);
    }

}
