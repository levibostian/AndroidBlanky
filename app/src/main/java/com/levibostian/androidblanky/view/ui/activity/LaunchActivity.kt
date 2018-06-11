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
import android.support.v7.app.AppCompatActivity
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.levibostian.androidblanky.service.auth.AccountAuthenticator
import com.levibostian.androidblanky.service.manager.NotificationChannelManager
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.androidblanky.view.ui.activity.auth.AuthenticatorActivity
import javax.inject.Inject

class LaunchActivity: Activity() {

    companion object {
        private const val LOGOUT_ACCOUNT = "LaunchActivity_LOGOUT_ACCOUNT"

        fun getIntent(context: Context, logoutOfAccount: Boolean): Intent {
            return Intent(context, LaunchActivity::class.java).apply {
                putExtra(LOGOUT_ACCOUNT, logoutOfAccount)
            }
        }
    }

    @Inject lateinit var userManager: UserManager
    @Inject lateinit var notificationChannelManager: NotificationChannelManager
    @Inject lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MainApplication).component.inject(this)

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
        if (passwordlessToken != null) {
            startActivity(AuthenticatorActivity.getIntent(this, passwordlessToken, false))
            finish()
            return
        }

        val userAccount = userManager.getAccount()
        if (userAccount == null || logoutOfAccount) { // The AccountAuthenticator's addAccount() function will simply launch an intent to launch the AuthenticatorActivity where it handles logging you out. If AccountAuthenticator is ever updated (there is a comment where), this code needs to be updated too to delete some data or something to trigger the logout process and THEN calling accountManager.addAccount().
            accountManager.addAccount(AccountAuthenticator.ACCOUNT_TYPE, null, null, null, this, {
                if (it.isDone && !it.isCancelled && it.result.getString(AccountManager.KEY_ACCOUNT_NAME) != null) startActivity(MainActivity.getIntent(this))
                finish()
            }, null)
        } else {
            accountManager.getAuthToken(userAccount, AccountAuthenticator.ACCOUNT_TYPE, null, this, {
                if (it.isDone && !it.isCancelled && it.result.getString(AccountManager.KEY_AUTHTOKEN) != null) startActivity(MainActivity.getIntent(this))
                finish()
            }, null)
        }
    }

    private fun getDynamicLinkIfExists(): Boolean {
        intent.data?.getQueryParameter("passwordless_token")?.let {
            launchStartupActivity(it, false)
            return true
        }

        // If a Firebase Dynamic short link launches the app, we cannot query for parameters in the intent. So, we do this hack  where we try to view the deep link which will launch this activity *again* but with a full length Dynamic link.
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