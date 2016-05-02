package com.levibostian.androidblanky.module;

import android.content.Context;
import android.content.SharedPreferences;
import com.levibostian.androidblanky.util.SharedPreferencesUtil;
import dagger.Module;
import dagger.Provides;

@Module
public class UtilModule {

    private Context mContext;

    public UtilModule(Context context) {
        mContext = context;
    }

    @Provides
    SharedPreferencesUtil provideSharedPreferencesUtil() {
        return new SharedPreferencesUtil(mContext);
    }

}
