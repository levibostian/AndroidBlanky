package com.levibostian.androidblanky.view.ui.activity.auth

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
import androidx.appcompat.app.AppCompatActivity
import com.levibostian.androidblanky.R
import com.levibostian.loadingemptyviews.widgets.LoadingView
import kotlinx.android.synthetic.main.activity_passwordless_email_login.*

class PasswordlessEmailLoginActivity: BasePasswordlessEmailLoginActivity() {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, PasswordlessEmailLoginActivity::class.java)
    }

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        setContentView(R.layout.activity_passwordless_email_login)
        setupViews()
    }

    private fun setupViews() {
        (act_passwordless_login_loading_empty.loadingView as LoadingView).loadingTextView!!.setTextColor(ContextCompat.getColor(this, android.R.color.white))

        act_passwordless_login_login_button.setOnClickListener {
            val enteredEmail = act_passwordless_login_email_edittext.text.toString()
            if (enteredEmail.isBlank()) {
                act_passwordless_login_email_edittext.error = getString(R.string.enter_email_address)
            } else {
                act_passwordless_login_loading_empty.showLoadingView(true)
                getPasswordlessToken(enteredEmail)
            }
        }

        act_passwordless_login_loading_empty.showContentView(false)
    }

    override fun getPasswordlessTokenSuccess() {
        AlertDialog.Builder(this)
                .setTitle(R.string.email_sent)
                .setMessage(R.string.check_email_to_finish_login)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.dismiss()

                    setResult(Activity.RESULT_OK)
                    finish()
                }
                .show()
    }

    override fun getPasswordlessTokenError(error: Throwable) {
        AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(error.message!!)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

}