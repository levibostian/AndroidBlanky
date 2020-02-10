package com.levibostian.androidblanky.service.manager

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.os.Build
import android.os.Build.VERSION_CODES.LOLLIPOP_MR1
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.levibostian.androidblanky.service.auth.AccountAuthenticator
import com.levibostian.androidblanky.view.ui.activity.auth.AuthenticatorActivity
import javax.inject.Inject

class DeviceAccountManager @Inject constructor(private val accountManager: AccountManager) {

    val accessToken: String?
        get() {
            getAccount()?.let { account ->
                return accountManager.peekAuthToken(account, AccountAuthenticator.ACCOUNT_TYPE)
            }
            return null
        }

    fun continueLoginFlow(activity: Activity, forceLogout: Boolean, passwordlessToken: String?, done: (success: Boolean) -> Unit) {
        val existingAccount = getAccount()

        if (existingAccount != null && !forceLogout) {
            /**
             * Verified that auth token exists for [Account]. An [Account] has been found which means it *should* exist and we don't want to logout, so we need to verify we can find it.
             *
             * This will call [AccountAuthenticator.getAuthToken] to verify. If it does not exist, an Intent will be started to show UI to get the token.
             */
            accountManager.getAuthToken(existingAccount, AccountAuthenticator.ACCOUNT_TYPE, null, activity, {
                val successfulLogin = it.isDone && !it.isCancelled && it.result.getString(AccountManager.KEY_AUTHTOKEN) != null
                done(successfulLogin)
            }, null)
        } else {
            /**
             * In this case, we are logging in for the first time, logging out, or exchanging passwordless token. No matter the reason, an [Account] was not found so we need to add one.
             */
            val addAccountOptions = Bundle().apply {
                putString(AuthenticatorActivity.PASSWORDLESS_TOKEN, passwordlessToken)
            }

            accountManager.addAccount(AccountAuthenticator.ACCOUNT_TYPE, null, null, addAccountOptions, activity, {
                val successfulLogin = it.isDone && !it.isCancelled && it.result.getString(AccountManager.KEY_ACCOUNT_NAME) != null
                done(successfulLogin)
            }, null)
        }
    }

    fun login(email: String, accessToken: String) {
        val account = Account(email, AccountAuthenticator.ACCOUNT_TYPE)
        accountManager.addAccountExplicitly(account, null, null)
        accountManager.setAuthToken(account, AccountAuthenticator.ACCOUNT_TYPE, accessToken)
    }

    fun logout() {
        accountManager.getAccountsByType(AccountAuthenticator.ACCOUNT_TYPE).forEach { account ->
            accountManager.clearPassword(account)
            accountManager.peekAuthToken(account, AccountAuthenticator.ACCOUNT_TYPE)?.let {
                accountManager.invalidateAuthToken(AccountAuthenticator.ACCOUNT_TYPE, it)
            }
            if (Build.VERSION.SDK_INT >= LOLLIPOP_MR1) {
                accountManager.removeAccountExplicitly(account)
            } else {
                accountManager.removeAccount(account, null, null, null)
            }
        }
    }

    private fun getAccount(): Account? {
        return accountManager.getAccountsByType(AccountAuthenticator.ACCOUNT_TYPE).getOrNull(0)
    }

}