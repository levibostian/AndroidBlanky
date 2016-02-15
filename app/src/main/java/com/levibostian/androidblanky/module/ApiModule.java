package com.levibostian.androidblanky.module;

import com.levibostian.androidblanky.MainApplication;
import com.levibostian.androidblanky.service.GitHubApi;
import dagger.Module;
import dagger.Provides;
import retrofit2.JacksonConverterFactory;
import retrofit2.Retrofit;

import javax.inject.Singleton;

@Module(library = true, complete = true)
public class ApiModule {

    private MainApplication mApplication;

    private static String API_BASE_URL = "https://api.github.com";

    public ApiModule(MainApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                       .baseUrl(API_BASE_URL)
                       .addConverterFactory(JacksonConverterFactory.create()).build();
    }

    @Provides
    public GitHubApi YummlyApi(Retrofit retrofit) {
        return retrofit.create(GitHubApi.class);
    }

}
