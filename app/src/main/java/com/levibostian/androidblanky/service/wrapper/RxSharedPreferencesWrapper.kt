package com.levibostian.androidblanky.service.wrapper

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences

class RxSharedPreferencesWrapper(sharedPreferences: SharedPreferences) {

    private val rxSharedPrefs: RxSharedPreferences  = RxSharedPreferences.create(sharedPreferences)

    fun getString(key: String): Preference<String> {
        return rxSharedPrefs.getString(key)
    }

}