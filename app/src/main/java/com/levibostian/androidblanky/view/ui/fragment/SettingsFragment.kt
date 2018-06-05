package com.levibostian.androidblanky.view.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.model.SharedPrefersKeys

class SettingsFragment: PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        fun getInstance(): SettingsFragment = SettingsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            getString(R.string.preferences_enable_analytics) -> {
                val enableAnalytics: Boolean = sharedPreferences?.getBoolean(SharedPrefersKeys.enableAnalytics(activity), true) ?: true
                FirebaseAnalytics.getInstance(activity).setAnalyticsCollectionEnabled(enableAnalytics)
            }
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()

        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()

        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

}