package com.levibostian.androidblanky.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.levibostian.androidblanky.manager.UserManager;
import com.levibostian.androidblanky.manager.api.ReposApiManager;
import com.levibostian.androidblanky.service.GitHubApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ManagerModule {

    private Context mContext;

    public ManagerModule(Context context) {
        mContext = context;
    }

    @Provides
    public UserManager provideUserManager(SharedPreferences sharedPreferences) {
        return new UserManager(mContext, sharedPreferences);
    }

    @Provides
    public ReposApiManager provideReposApiManager(GitHubApi api, Retrofit retrofit) {
        return new ReposApiManager(api, retrofit);
    }

}
