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
import com.levibostian.androidblanky.extensions.onCreateDiGraph
import com.levibostian.androidblanky.service.manager.UserManager
import kotlinx.android.synthetic.main.activity_unlock_loggedin_account.*
import java.util.*
import javax.inject.Inject

class UnlockLoggedInAccountActivity: BasePasswordlessEmailLoginActivity() {

    enum class SwapperViews {
        LOADING_VIEW,
        UNLOCK_ACCOUNT_VIEW
    }

    companion object {
        const val LOGOUT_ANYWAY_RESULT_KEY = "UnlockLoggedInAccountActivity.LOGOUT_ANYWAY_RESULT_KEY"

        fun getIntent(context: Context): Intent = Intent(context, UnlockLoggedInAccountActivity::class.java)
    }

    @Inject lateinit var userManager: UserManager

    override fun onCreate(icicle: Bundle?) {
        onCreateDiGraph().inject(this)
        super.onCreate(icicle)

        setContentView(R.layout.activity_unlock_loggedin_account)
        setupViews()
    }

    private fun setupViews() {
        act_unlock_account_message_textview.text = String.format(Locale.getDefault(), getString(R.string.unlock_account_message), userManager.email!!)

        act_unlock_account_login_button.setOnClickListener {
            act_unlock_account_swapper_view.swapTo(SwapperViews.LOADING_VIEW.name) {
                getPasswordlessToken(userManager.email!!)
            }
        }
        act_unlock_account_logout_anyway_button.setOnClickListener {
            unlockAccountResult(logoutAnyway = true)
        }

        act_unlock_account_swapper_view.apply {
            viewMap = mapOf(
                    Pair(SwapperViews.LOADING_VIEW.name, act_unlock_account_loading_view),
                    Pair(SwapperViews.UNLOCK_ACCOUNT_VIEW.name, act_unlock_account_login_view)
            )
            swapTo(SwapperViews.UNLOCK_ACCOUNT_VIEW.name) {}
        }
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