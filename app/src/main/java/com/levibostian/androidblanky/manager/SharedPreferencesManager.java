package com.levibostian.androidblanky.manager;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class SharedPreferencesManager {

    protected static final int INVALID_ID = -1;

    protected Context mContext;
    protected SharedPreferences mSharedPreferences;

    public interface EditSharedPreferences {
        void edit(SharedPreferences.Editor editor);
    }

    protected SharedPreferencesManager(Context context, SharedPreferences sharedPreferences) {
        mContext = context;
        mSharedPreferences = sharedPreferences;
    }

    protected String getStringFromSharedPreferences(int keyStringRes) {
        return mSharedPreferences.getString(mContext.getString(keyStringRes), null);
    }

    protected int getIntFromSharedPreferences(int keyStringRes) {
        return mSharedPreferences.getInt(mContext.getString(keyStringRes), INVALID_ID);
    }

    protected boolean getBooleanFromSharedPreferences(int keyStringRes) {
        return mSharedPreferences.getBoolean(mContext.getString(keyStringRes), false);
    }

    protected void editSharedPreferences(EditSharedPreferences editListener) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editListener.edit(editor);

        editor.commit();
    }

}
