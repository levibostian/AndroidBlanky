package com.levibostian.androidblanky.view.ui.activity.auth

import android.accounts.AccountAuthenticatorActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.accounts.AccountManager
import android.accounts.Account
import android.app.Activity
import com.levibostian.androidblanky.extensions.onCreateDiGraph
import com.levibostian.androidblanky.service.auth.AccountAuthenticator
import com.levibostian.androidblanky.service.logger.Logger
import com.levibostian.androidblanky.service.manager.DeviceAccountManager
import com.levibostian.androidblanky.service.manager.UserManager
import javax.inject.Inject

class AuthenticatorActivity: AccountAuthenticatorActivity() {

    companion object {
        const val PASSWORDLESS_TOKEN = "AuthenticatorActivity.PASSWORDLESS_TOKEN"

        private const val PASSWORDLESS_TOKEN_EXCHANGE_REQUEST_ID = 0
        private const val LOGOUT_REQUEST_ID = 1
        private const val UNLOCK_ACCOUNT_REQUEST_ID = 2
        private const val PASSWORDLESS_EMAIL_LOGIN_REQUEST_ID = 3

        fun getIntent(context: Context, passwordlessToken: String?): Intent = Intent(context, AuthenticatorActivity::class.java).apply {
            putExtra(PASSWORDLESS_TOKEN, passwordlessToken)
        }
    }

    @Inject lateinit var userManager: UserManager
    @Inject lateinit var deviceAccountManager: DeviceAccountManager
    @Inject lateinit var logger: Logger

    private val passwordlessToken: String?
        get() = intent?.extras?.getString(PASSWORDLESS_TOKEN)

    override fun onCreate(icicle: Bundle?) {
        onCreateDiGraph().inject(this)
        super.onCreate(icicle)

        passwordlessToken?.let { passwordlessToken ->
            exchangePasswordlessTokenForAuth(passwordlessToken)
        } ?: kotlin.run {
            when {
                // Activity launched with intent of logging the user out since they are still technically logged in.
                userManager.isUserLoggedIn() -> logout()
                // User not logged in, but account data exists (user installed app and Android restored a previous data backup, or a 401 http status code). Re-prompt them to login to "unlock" their account and data.
                userManager.doesUserAccountNeedUnlocked() -> unlockLoggedInUserAccount()
                // We are showing this activity for the first login. Do the logout flow to assert that all data is destroyed first.
                else -> logout()
            }
        }
    }

    private fun exchangePasswordlessTokenForAuth(passwordlessToken: String) {
        startActivityForResult(PasswordTokenExchangeActivity.getIntent(this, passwordlessToken), PASSWORDLESS_TOKEN_EXCHANGE_REQUEST_ID)
    }

    private fun startPasswordlessEmailLogin() {
        startActivityForResult(PasswordlessEmailLoginActivity.getIntent(this), PASSWORDLESS_EMAIL_LOGIN_REQUEST_ID)
    }

    private fun unlockLoggedInUserAccount() {
        startActivityForResult(UnlockLoggedInAccountActivity.getIntent(this), UNLOCK_ACCOUNT_REQUEST_ID)
    }

    private fun logout() {
        startActivityForResult(LogoutAccountActivity.getIntent(this), LOGOUT_REQUEST_ID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            PASSWORDLESS_TOKEN_EXCHANGE_REQUEST_ID -> {
                val userId = intent!!.getStringExtra(PasswordTokenExchangeActivity.USER_ID_RESULT_KEY)!!
                val userEmail = intent!!.getStringExtra(PasswordTokenExchangeActivity.USER_EMAIL_RESULT_KEY)!!
                val authToken = intent!!.getStringExtra(PasswordTokenExchangeActivity.AUTH_TOKEN_RESULT_KEY)!!

                finishLogin(userId, userEmail, authToken)
            }
            LOGOUT_REQUEST_ID -> {
                startPasswordlessEmailLogin()
            }
            UNLOCK_ACCOUNT_REQUEST_ID -> {
                val unlockAnyway = intent!!.getBooleanExtra(UnlockLoggedInAccountActivity.LOGOUT_ANYWAY_RESULT_KEY, false)
                if (unlockAnyway) logout()
                else unlockLoggedInUserAccount() // We don't have any other UI to share, so just show the unlock UI again.
            }
            PASSWORDLESS_EMAIL_LOGIN_REQUEST_ID -> {
                // since we have no other UI to show to the user, just show the login one again.
                startPasswordlessEmailLogin()
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun finishLogin(userId: String, email: String, authToken: String) {
        userManager.id = userId
        userManager.email = email

        logger.loggedIn(userId, Logger.LoginMethod.PasswordlessEmail())

        val res = Intent()
        res.putExtra(AccountManager.KEY_ACCOUNT_NAME, email)
        res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AccountAuthenticator.ACCOUNT_TYPE)
        res.putExtra(AccountManager.KEY_AUTHTOKEN, authToken)

        deviceAccountManager.login(email, authToken)

        setAccountAuthenticatorResult(res.extras)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}