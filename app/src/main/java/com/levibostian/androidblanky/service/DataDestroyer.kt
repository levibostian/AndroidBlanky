package com.levibostian.androidblanky.service

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Build
import android.os.Build.VERSION_CODES.LOLLIPOP_MR1
import android.os.Handler
import androidx.core.content.edit
import com.levibostian.androidblanky.service.auth.AccountAuthenticator
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.manager.DeviceAccountManager
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.testing.OpenForTesting
import com.levibostian.wendy.service.Wendy

@OpenForTesting
class DataDestroyer(private val db: Database,
                    private val deviceAccountManager: DeviceAccountManager,
                    private val userManager: UserManager,
                    private val sharedPreferences: SharedPreferences) {

    fun destroyAll(complete: (() -> Unit)?) {
        DataDestroyerDestroyAllAsyncTask(this) { error ->
            error?.let { throw it }
            complete?.invoke()
        }.execute()
    }

    fun destroySqlite() {
        db.clearAllTables()
    }

    fun destroyAccountManagerAccounts() {
        deviceAccountManager.logout()
    }

    @SuppressLint("ApplySharedPref")
    fun destroySharedPreferences() {
        sharedPreferences.edit {
            clear()
        }
    }

    fun destroyWendy(complete: () -> Unit?) {
        Wendy.shared.clearAsync(complete)
    }

    private class DataDestroyerDestroyAllAsyncTask(private val destroyer: DataDestroyer, private val complete: (error: Throwable?) -> Unit?): AsyncTask<Unit?, Unit?, Unit?>() {

        private var doInBackgroundError: Throwable? = null

        override fun doInBackground(vararg p: Unit?): Unit? {
            try {
                destroyer.destroySqlite()
                destroyer.destroyAccountManagerAccounts()
                destroyer.destroySharedPreferences()
                destroyer.userManager.logout()
                Wendy.shared.clear()
            } catch (e: Throwable) {
                doInBackgroundError = e
            }

            return null
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            complete(doInBackgroundError)
        }

    }

}