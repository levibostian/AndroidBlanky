package com.levibostian.androidblanky.service.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import com.levibostian.androidblanky.view.ui.MainApplication
import javax.inject.Inject

class AppFirstLaunchBroadcastReceiver: BroadcastReceiver() {

    @SuppressLint("ApplySharedPref")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return
        if (intent.action != Intent.ACTION_PACKAGE_FIRST_LAUNCH) return
        if (intent.dataString != context.packageName) return
        if (intent.`package` != context.packageName) return

        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(SharedPrefersKeys.FIRST_APP_LAUNCH, true)
                .commit()
    }

}