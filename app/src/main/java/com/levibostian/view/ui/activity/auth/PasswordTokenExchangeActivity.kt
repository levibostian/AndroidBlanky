package com.levibostian.view.ui.activity.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.levibostian.R

class PasswordTokenExchangeActivity: AppCompatActivity() {

    companion object {
        private const val PASSWORDLESS_TOKEN = "PasswordTokenExchangeActivity.PASSWORDLESS_TOKEN"

        const val AUTH_TOKEN_RESULT_KEY = "PasswordTokenExchangeActivity.AUTH_TOKEN_RESULT_KEY"
        const val USER_ID_RESULT_KEY = "PasswordTokenExchangeActivity.USER_ID_RESULT_KEY"
        const val USER_EMAIL_RESULT_KEY = "PasswordTokenExchangeActivity.USER_EMAIL_RESULT_KEY"

        fun getIntent(context: Context, passwordlessToken: String): Intent = Intent(context, PasswordTokenExchangeActivity::class.java).apply {
            putExtra(AuthenticatorActivity.PASSWORDLESS_TOKEN, passwordlessToken)
        }
    }

    private val passwordlessToken: String
        get() = intent!!.extras!!.getString(PASSWORDLESS_TOKEN)!!

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        setContentView(R.layout.activity_password_token)
        setupViews()
    }

    private fun setupViews() {
    }

    override fun onStart() {
        super.onStart()

        // TODO exchange the passwordlessToken with server to get an auth token.
        // Instead, we will pretend we have a server and count down and finish.

        Handler().postDelayed({
            val fakeId = "1"
            val fakeEmail = "foo@foo.com"
            val fakeAuthToken = "123"
            passwordlessTokenExchangeSuccess(fakeId, fakeEmail, fakeAuthToken)
        }, 3000)
    }

    private fun passwordlessTokenExchangeSuccess(userId: String, email: String, authToken: String) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(USER_ID_RESULT_KEY, userId)
            putExtra(USER_EMAIL_RESULT_KEY, email)
            putExtra(AUTH_TOKEN_RESULT_KEY, authToken)
        })
        finish()
    }

}