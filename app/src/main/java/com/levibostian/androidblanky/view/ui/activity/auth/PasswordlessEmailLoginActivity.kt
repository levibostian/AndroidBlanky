package com.levibostian.androidblanky.view.ui.activity.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.extensions.onCreateDiGraph
import kotlinx.android.synthetic.main.activity_passwordless_email_login.*

class PasswordlessEmailLoginActivity: BasePasswordlessEmailLoginActivity() {

    enum class SwapperViews {
        LOADING_VIEW,
        LOGIN_VIEW
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, PasswordlessEmailLoginActivity::class.java)
    }

    override fun onCreate(icicle: Bundle?) {
        onCreateDiGraph().inject(this)
        super.onCreate(icicle)

        setContentView(R.layout.activity_passwordless_email_login)
        setupViews()
    }

    private fun setupViews() {
        act_passwordless_login_login_button.setOnClickListener {
            val enteredEmail = act_passwordless_login_email_edittext.text.toString()
            if (enteredEmail.isBlank()) {
                act_passwordless_login_email_edittext.error = getString(R.string.enter_email_address)
            } else {
                act_passwordless_login_swapper.swapTo(SwapperViews.LOADING_VIEW.name) {
                    getPasswordlessToken(enteredEmail)
                }
            }
        }

        act_passwordless_login_swapper.apply {
            viewMap = mapOf(
                    Pair(SwapperViews.LOADING_VIEW.name, act_passwordless_login_loadingview),
                    Pair(SwapperViews.LOGIN_VIEW.name, act_passwordless_login_loginview)
            )
            swapTo(SwapperViews.LOGIN_VIEW.name) {}
        }
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