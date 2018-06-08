package com.levibostian.androidblanky.service

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Build
import android.os.Build.VERSION_CODES.LOLLIPOP_MR1
import android.os.Handler
import com.levibostian.androidblanky.service.auth.AccountAuthenticator
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.wendy.service.Wendy

class DataDestroyer(private val db: Database,
                    private val accountManager: AccountManager,
                    private val userManager: UserManager,
                    private val sharedPreferences: SharedPreferences) {

    fun destroyAll(complete: (() -> Unit)?) {
        DataDestroyerDestroyAllAsyncTask(this, { error ->
            error?.let { throw it }
            complete?.invoke()
        }).execute()
    }

    fun destroySqlite() {
        db.clearAllTables()
    }

    fun destroyAccountManagerAccounts() {
        accountManager.getAccountsByType(AccountAuthenticator.ACCOUNT_TYPE).forEach { account ->
            accountManager.clearPassword(account)
            accountManager.peekAuthToken(account, AccountAuthenticator.ACCOUNT_TYPE)?.let {
                accountManager.invalidateAuthToken(AccountAuthenticator.ACCOUNT_TYPE, it)
            }
            if (Build.VERSION.SDK_INT >= LOLLIPOP_MR1) {
                accountManager.removeAccount(account, null, null, null)
            } else {
                accountManager.removeAccount(account, null, null)
            }
        }
    }

    @SuppressLint("ApplySharedPref")
    fun destroySharedPreferences() {
        sharedPreferences.edit().clear().commit()
    }

    fun destroyWendy() {
    }

    private class DataDestroyerDestroyAllAsyncTask(private val destroyer: DataDestroyer, private val complete: (error: Throwable?) -> Unit?): AsyncTask<Unit?, Unit?, Unit?>() {

        private var doInBackgroundException: Throwable? = null

        override fun doInBackground(vararg p: Unit?): Unit? {
            try {
                destroyer.destroySqlite()
                destroyer.destroyAccountManagerAccounts()
                destroyer.destroySharedPreferences()
                destroyer.destroyWendy()
                destroyer.userManager.logout()
            } catch (e: Throwable) {
                doInBackgroundException = e
            }

            return null
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            complete(doInBackgroundException)
        }

    }

}