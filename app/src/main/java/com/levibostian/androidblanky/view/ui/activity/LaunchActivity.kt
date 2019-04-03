package com.levibostian.androidblanky.view.ui.activity

import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.levibostian.androidblanky.service.auth.AccountAuthenticator
import com.levibostian.androidblanky.service.manager.DeviceAccountManager
import com.levibostian.androidblanky.service.manager.NotificationChannelManager
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.androidblanky.view.ui.activity.auth.AuthenticatorActivity
import org.koin.android.ext.android.inject

class LaunchActivity: Activity() {

    companion object {
        private const val LOGOUT_ACCOUNT = "LaunchActivity_LOGOUT_ACCOUNT"

        fun getIntent(context: Context, logoutOfAccount: Boolean): Intent {
            return Intent(context, LaunchActivity::class.java).apply {
                putExtra(LOGOUT_ACCOUNT, logoutOfAccount)
            }
        }
    }

    private val notificationChannelManager: NotificationChannelManager by inject()
    private val deviceAccountManager: DeviceAccountManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationChannelManager.createChannels()

        val logoutAccount: Boolean = intent?.extras?.getBoolean(LOGOUT_ACCOUNT) ?: false
        if (logoutAccount) {
            launchStartupActivity(null, true)
        } else {
            if (!getDynamicLinkIfExists()) {
                launchStartupActivity(null, false)
            }
        }
    }

    private fun launchStartupActivity(passwordlessToken: String?, logoutOfAccount: Boolean) {
        deviceAccountManager.continueLoginFlow(this, logoutOfAccount, passwordlessToken) { userLoggedIn ->
            if (userLoggedIn) startActivity(MainActivity.getIntent(this))
            finish()
        }
    }

    private fun getDynamicLinkIfExists(): Boolean {
        intent?.data?.getQueryParameter("passwordless_token")?.let {
            launchStartupActivity(it, false)
            return true
        }

        // If a Firebase Dynamic short link launches the app, we cannot query for parameters in the intent. So, we do this hack where we try to view the deep link which will launch this activity *again* but with a full length Dynamic link.
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    pendingDynamicLinkData?.link?.let { deepLink ->
                        startActivity(Intent(Intent.ACTION_VIEW, deepLink))
                        finish()
                    }
                }
                .addOnFailureListener(this) { error -> throw error }
        return false
    }

}