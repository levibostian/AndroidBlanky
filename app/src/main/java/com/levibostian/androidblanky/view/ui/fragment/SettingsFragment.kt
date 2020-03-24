package com.levibostian.androidblanky.view.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.model.SharedPrefersKeys

class SettingsFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            getString(R.string.preferences_enable_analytics) -> {
                val enableAnalytics: Boolean = sharedPreferences?.getBoolean(SharedPrefersKeys.enableAnalytics(requireActivity()), true) ?: true
                FirebaseAnalytics.getInstance(requireActivity()).setAnalyticsCollectionEnabled(enableAnalytics)
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