package com.levibostian.androidblanky.view.ui.activity.auth

import android.accounts.AccountAuthenticatorActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.accounts.AccountManager
import android.accounts.Account
import android.app.Activity
import android.app.ProgressDialog
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.DataDestroyer
import com.levibostian.androidblanky.service.auth.AccountAuthenticator
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.view.ui.MainApplication
import kotlinx.android.synthetic.main.activity_authenticator.*
import java.util.*
import javax.inject.Inject

class AuthenticatorActivity: AccountAuthenticatorActivity() {

    companion object {
        const val PASSWORDLESS_TOKEN = "AuthenticatorActivity_PASSWORDLESS_TOKEN"
        const val FORCE_LOGOUT = "AuthenticatorActivity_FORCE_LOGOUT"

        fun getIntent(context: Context, passwordlessToken: String?, forceLogout: Boolean): Intent {
            return Intent(context, AuthenticatorActivity::class.java).apply {
                passwordlessToken?.let { putExtra(PASSWORDLESS_TOKEN, it) }
                putExtra(FORCE_LOGOUT, forceLogout)
            }
        }
    }

    @Inject lateinit var accountManager: AccountManager
    @Inject lateinit var userManager: UserManager
    @Inject lateinit var dataDestroyer: DataDestroyer

    private var passwordlessToken: String? = null
    private var forceLogout: Boolean = false

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        (application as MainApplication).component.inject(this)

        setContentView(R.layout.activity_authenticator)
        setupViews()

        passwordlessToken = intent.extras?.getString(PASSWORDLESS_TOKEN) ?: icicle?.getString(PASSWORDLESS_TOKEN)
        if (passwordlessToken != null) {
            showLoadingView(getString(R.string.exchange_passwordless_token_auth_token_loading_message))

            // TODO call server to exchange token with a auth token
            // TODO call finishLogin() after getting the auth token and email from the server.
        } else {
            fun deleteAllDataAndStartLogin(message: String) {
                showLoadingView(message)
                dataDestroyer.destroyAll({
                    showLoginView()
                })
            }

            forceLogout = intent.extras?.getBoolean(FORCE_LOGOUT) ?: icicle?.getBoolean(FORCE_LOGOUT) ?: false
            if (!forceLogout && userManager.isUserLoggedIn()) {
                val loggedInUserEmail: String = userManager.getAccount()!!.name
                AlertDialog.Builder(this)
                        .setTitle(R.string.sure_logout)
                        .setMessage(String.format(Locale.getDefault(), getString(R.string.sure_logout_message), loggedInUserEmail, loggedInUserEmail))
                        .setPositiveButton(R.string.yes, { dialog, which ->
                            deleteAllDataAndStartLogin(getString(R.string.logging_out))
                        })
                        .setNegativeButton(R.string.no, { dialog, which ->
                            dialog.dismiss()
                            finish()
                        })
                        .show()
            } else {
                val clearDataMessage = getString(if (forceLogout) R.string.logging_out else R.string.clearing_data)
                deleteAllDataAndStartLogin(clearDataMessage)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(FORCE_LOGOUT, forceLogout)
        passwordlessToken?.let { outState?.putString(PASSWORDLESS_TOKEN, passwordlessToken) }

        super.onSaveInstanceState(outState)
    }

    private fun setupViews() {
        auth_activity_login_button.setOnClickListener {
            val enteredEmail = auth_activity_email_edittext.text.toString()
            if (enteredEmail.isBlank()) {
                auth_activity_email_edittext.error = getString(R.string.enter_email_address)
            } else {
                // TODO send email up to server to have it begin login process. In this case, send an email in a "passwordless" fashion.

                // TODO remove me below...
                Handler().postDelayed({ // Pretending that we got our email and auth token back from the server.
                    val email = "you@example.com"
                    val authToken = "12345"

                    finishLogin("1", email, authToken)
                }, 4000)

                AlertDialog.Builder(this)
                        .setTitle(R.string.email_sent)
                        .setMessage(R.string.check_email_to_finish_login)
                        .setPositiveButton(R.string.ok, { dialog, _ ->
                            dialog.dismiss()
                        })
                        .show()
            }
        }
        auth_activity_loading_view.loadingTextView!!.setTextColor(ContextCompat.getColor(this, android.R.color.white))
    }

    private fun showLoadingView(message: String) {
        auth_activity_loading_view.loadingText = message

        auth_activity_login_view.visibility = View.GONE
        auth_activity_loading_view.visibility = View.VISIBLE
    }

    private fun showLoginView() {
        auth_activity_login_view.visibility = View.VISIBLE
        auth_activity_loading_view.visibility = View.GONE
    }

    private fun finishLogin(userId: String, email: String, authToken: String) {
        userManager.id = userId

        val res = Intent()
        res.putExtra(AccountManager.KEY_ACCOUNT_NAME, email)
        res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AccountAuthenticator.ACCOUNT_TYPE)
        res.putExtra(AccountManager.KEY_AUTHTOKEN, authToken)

        val account = Account(email, AccountAuthenticator.ACCOUNT_TYPE)
        accountManager.addAccountExplicitly(account, null, null)
        accountManager.setAuthToken(account, AccountAuthenticator.ACCOUNT_TYPE, authToken)

        setAccountAuthenticatorResult(res.extras)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}