package com.levibostian.androidblanky.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.levibostian.androidblanky.R;

public class SharedPreferencesUtil {

    public static final int INVALID_INT = -1;

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public SharedPreferencesUtil(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
    }

    public void save(String value, String key) {
        getEditor().putString(key, value).commit();
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, INVALID_INT);
    }

}
