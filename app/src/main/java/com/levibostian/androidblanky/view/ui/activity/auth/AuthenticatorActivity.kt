package com.levibostian.androidblanky.view.ui.activity.auth

import android.accounts.AccountAuthenticatorActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.accounts.AccountManager
import android.accounts.Account
import android.app.Activity
import android.os.Handler
import androidx.core.content.ContextCompat
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.DataDestroyer
import com.levibostian.androidblanky.service.auth.AccountAuthenticator
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.view.ui.MainApplication
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_authenticator.*
import java.util.*
import javax.inject.Inject

class AuthenticatorActivity: AccountAuthenticatorActivity() {

    companion object {
        const val PASSWORDLESS_TOKEN = "AuthenticatorActivity_PASSWORDLESS_TOKEN"
        const val FORCE_LOGOUT = "AuthenticatorActivity_FORCE_LOGOUT"

        fun getIntent(context: Context): Intent = Intent(context, AuthenticatorActivity::class.java)
    }

    @Inject lateinit var accountManager: AccountManager
    @Inject lateinit var userManager: UserManager
    @Inject lateinit var dataDestroyer: DataDestroyer

    private var state: State? = null
        set(value) {
            field = value

            when (value) {
                AuthenticatorActivity.State.LOGGING_OUT_LOADING -> {
                    auth_activity_loading_view.loadingText = getString(R.string.logging_out)
                    auth_activity_login_view.visibility = View.GONE
                    auth_activity_loading_view.visibility = View.VISIBLE
                }
                AuthenticatorActivity.State.LOGIN -> {
                    auth_activity_login_view.visibility = View.VISIBLE
                    auth_activity_loading_view.visibility = View.GONE
                    auth_activity_email_textview.visibility = View.GONE
                    auth_activity_email_edittext.visibility = View.VISIBLE
                    auth_activity_login_button.text = getString(R.string.done)
                    auth_activity_logout_anyway_view.visibility = View.GONE
                    auth_activity_message_textview.text = getString(R.string.login_message)
                }
                AuthenticatorActivity.State.UNLOCK_ACCOUNT -> {
                    auth_activity_login_view.visibility = View.VISIBLE
                    auth_activity_loading_view.visibility = View.GONE
                    auth_activity_email_textview.visibility = View.VISIBLE
                    auth_activity_email_edittext.visibility = View.GONE
                    auth_activity_login_button.text = getString(R.string.unlock)
                    auth_activity_email_textview.text = String.format(Locale.getDefault(), "email: %s", userManager.email!!)
                    auth_activity_logout_anyway_view.visibility = View.VISIBLE
                    auth_activity_message_textview.text = String.format(Locale.getDefault(), getString(R.string.unlock_account_message), userManager.email!!)
                }
                AuthenticatorActivity.State.FIRST_TIME_LOGIN_LOADING -> {
                    auth_activity_loading_view.loadingText = getString(R.string.preparing_for_login)
                    auth_activity_login_view.visibility = View.GONE
                    auth_activity_loading_view.visibility = View.VISIBLE
                }
                AuthenticatorActivity.State.PASSWORD_TOKEN_EXCHANGE -> {
                    auth_activity_loading_view.loadingText = getString(R.string.exchange_passwordless_token_auth_token_loading_message)
                    auth_activity_login_view.visibility = View.GONE
                    auth_activity_loading_view.visibility = View.VISIBLE
                }
            }
        }

    private var passwordlessToken: String? = null
    private var forceLogout: Boolean = false

    override fun onCreate(icicle: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(icicle)

        setContentView(R.layout.activity_authenticator)
        setupViews()

        passwordlessToken = intent?.extras?.getString(PASSWORDLESS_TOKEN) ?: icicle?.getString(PASSWORDLESS_TOKEN)
        if (passwordlessToken != null) {
            state = State.PASSWORD_TOKEN_EXCHANGE

            // TODO call server to exchange token with a auth token. Do this in a ViewModel!
            // TODO call finishLogin() after getting the auth token and email from the server.
        } else {
            forceLogout = intent?.extras?.getBoolean(FORCE_LOGOUT) ?: icicle?.getBoolean(FORCE_LOGOUT) ?: false
            when {
                forceLogout -> {
                    state = State.LOGGING_OUT_LOADING
                    dataDestroyer.destroyAll {
                        state = State.LOGIN
                    }
                }
                userManager.isUserLoggedIn() -> // Activity launched with intent of logging the user out since they are still technically logged in.
                    confirmIfUserWantsToLogout()
                userManager.doesUserAccountNeedUnlocked() -> // User not logged in, but account data exists (user installed app and Android restored a previous data backup, or a 401 http status code). Re-prompt them to login to "unlock" their account and data.
                    state = State.UNLOCK_ACCOUNT
                else -> { // We are showing this activity for the first login.
                    state = State.FIRST_TIME_LOGIN_LOADING
                    dataDestroyer.destroyAll {
                        state = State.LOGIN
                    }
                }
            }
        }
    }

    private fun confirmIfUserWantsToLogout() {
        val loggedInUserEmail: String = userManager.email!!
        AlertDialog.Builder(this)
                .setTitle(R.string.sure_logout)
                .setMessage(String.format(Locale.getDefault(), getString(R.string.sure_logout_message), loggedInUserEmail, loggedInUserEmail))
                .setPositiveButton(R.string.yes) { dialog, which ->
                    state = State.LOGGING_OUT_LOADING
                    dataDestroyer.destroyAll {
                        state = State.LOGIN
                    }
                }
                .setNegativeButton(R.string.no) { dialog, which ->
                    dialog.dismiss()
                    finish()
                }
                .show()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(FORCE_LOGOUT, forceLogout)
        passwordlessToken?.let { outState?.putString(PASSWORDLESS_TOKEN, passwordlessToken) }

        super.onSaveInstanceState(outState)
    }

    private fun setupViews() {
        auth_activity_login_button.setOnClickListener {
            if (state == State.UNLOCK_ACCOUNT) {
                // TODO Call API to send email and get auth token back for user.
                
                // TODO remove handler below.
                Handler().postDelayed({ // Pretending that we got our email and auth token back from the server.
                    val email = "you@example.com"
                    val authToken = "12345"

                    finishLogin("1", email, authToken)
                }, 4000)

                AlertDialog.Builder(this)
                        .setTitle(R.string.email_sent)
                        .setMessage(R.string.check_email_to_finish_login)
                        .setPositiveButton(R.string.ok) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
            } else if (state == State.LOGIN) {
                val enteredEmail = auth_activity_email_edittext.text.toString()
                if (enteredEmail.isBlank()) {
                    auth_activity_email_edittext.error = getString(R.string.enter_email_address)
                } else {
                    // TODO send email up to server to have it begin login process. In this case, send an email in a "passwordless" fashion.
                    // TODO make sure to do this in a ViewModel!

                    // TODO remove me below...
                    Handler().postDelayed({ // Pretending that we got our email and auth token back from the server.
                        val email = "you@example.com"
                        val authToken = "12345"

                        finishLogin("1", email, authToken)
                    }, 4000)

                    AlertDialog.Builder(this)
                            .setTitle(R.string.email_sent)
                            .setMessage(R.string.check_email_to_finish_login)
                            .setPositiveButton(R.string.ok) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                }
            }
        }
        auth_activity_loading_view.loadingTextView!!.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        auth_activity_logout_anyway_button.setOnClickListener {
            confirmIfUserWantsToLogout()
        }
    }

    private fun finishLogin(userId: String, email: String, authToken: String) {
        userManager.id = userId
        userManager.email = email

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

    private enum class State {
        LOGGING_OUT_LOADING, // Logging the user out of the app.
        FIRST_TIME_LOGIN_LOADING, // No data from backup, no logging out, first time logging in. Loading view before LOGIN.
        LOGIN, // User logged out or it's their first time logging in.
        UNLOCK_ACCOUNT, // User account does not exist in AccountManager, but data exists in the app. This means we need to show a UI for the user to login again to "unlock" their data.
        PASSWORD_TOKEN_EXCHANGE // Activity launched with intent to exchange passwordless token with auth token.
    }

}