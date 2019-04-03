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
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.loadingemptyviews.widgets.LoadingView
import kotlinx.android.synthetic.main.activity_unlock_loggedin_account.*
import org.koin.android.ext.android.inject
import java.util.*

class UnlockLoggedInAccountActivity: BasePasswordlessEmailLoginActivity() {

    companion object {
        const val LOGOUT_ANYWAY_RESULT_KEY = "UnlockLoggedInAccountActivity.LOGOUT_ANYWAY_RESULT_KEY"

        fun getIntent(context: Context): Intent = Intent(context, UnlockLoggedInAccountActivity::class.java)
    }

    private val userManager: UserManager by inject()

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        setContentView(R.layout.activity_unlock_loggedin_account)
        setupViews()
    }

    private fun setupViews() {
        (act_unlock_account_loading_empty.loadingView as LoadingView).loadingTextView!!.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        act_unlock_account_message_textview.text = String.format(Locale.getDefault(), getString(R.string.unlock_account_message), userManager.email!!)

        act_unlock_account_login_button.setOnClickListener {
            act_unlock_account_loading_empty.showLoadingView(true)
            getPasswordlessToken(userManager.email!!)
        }
        act_unlock_account_logout_anyway_button.setOnClickListener {
            unlockAccountResult(logoutAnyway = true)
        }

        act_unlock_account_loading_empty.showContentView(false)
    }

    override fun getPasswordlessTokenSuccess() {
        AlertDialog.Builder(this)
                .setTitle(R.string.email_sent)
                .setMessage(R.string.check_email_to_finish_login)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.dismiss()

                    unlockAccountResult(logoutAnyway = false)
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

    private fun unlockAccountResult(logoutAnyway: Boolean) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(LOGOUT_ANYWAY_RESULT_KEY, logoutAnyway)
        })
        finish()
    }

}