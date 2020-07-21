package com.app.view.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.app.R
import com.app.extensions.onAttachDiGraph
import com.app.service.KeyValueStorage
import com.app.service.KeyValueStorageKey
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject lateinit var keyValueStorage: KeyValueStorage

    override fun onAttach(context: Context) {
        onAttachDiGraph().inject(this)
        super.onAttach(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            getString(R.string.preferences_enable_analytics) -> {
                val enableAnalytics: Boolean = keyValueStorage.bool(KeyValueStorageKey.LOGGED_IN_USER_AUTH_TOKEN)
                Firebase.analytics.setAnalyticsCollectionEnabled(enableAnalytics)
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
